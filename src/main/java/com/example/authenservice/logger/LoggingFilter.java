package com.example.authenservice.logger;

import ch.qos.logback.classic.ClassicConstants;
import com.example.loggerapi.utils.LoggerUtils2;
import com.example.loggerapi.wrapper.MyHttpServletReponseWrapper;
import com.example.loggerapi.wrapper.MyHttpServletRequestWrapper;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class LoggingFilter extends GenericFilterBean {
    public static final String TRANS_ID_REQUEST = "TRANS_ID";
    public static final String SPAN_ID_REQUEST = "SPAN_ID";
    public static final String CLIENT_IP = "clientIP";
    public static final String HTTP_METHOD = "httpMethod";
    public static final String DURATION_TIME = "durationTime";
    protected static final int MAX_CONTENT_LENGTH_REQUEST_LOG = 1024 * 32; // 32 KB
    protected static final int MAX_CONTENT_LENGTH_RESPONSE_LOG = 1024 * 32; // 32 KB



    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            long startTime = System.nanoTime();

            // build wrapper class from both of request, response to reuse data
            MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper((HttpServletRequest) request);
            MyHttpServletReponseWrapper responseWrapper = new MyHttpServletReponseWrapper((HttpServletResponse) response);


            String traceId = MDC.get("X-B3-TraceId");
            String spanId = MDC.get("X-B3-SpanId");

            String operatorName = requestWrapper.getRequestURI();
            String clientIP = request.getRemoteAddr();
            String httpMethod = requestWrapper.getMethod();

            buildMDC(clientIP, httpMethod);

            Map<String, String> extraParam = extraParams((HttpServletRequest) request);

            // write unique id to servlet request
            requestWrapper.setAttribute(TRANS_ID_REQUEST, traceId);
            requestWrapper.setAttribute(SPAN_ID_REQUEST, spanId);

            // write request log
            writeRequestLog(operatorName, requestWrapper, extraParam);

            try {
                chain.doFilter(requestWrapper, responseWrapper);
            } catch (Exception e) {
                long endTime = System.nanoTime();
                double durationTime = (endTime - startTime) / 1000000000.0;
                MDC.put(DURATION_TIME, String.format("%.2f", durationTime));

                writeErrorLog(operatorName, requestWrapper, responseWrapper, e);

                response.getOutputStream().write(responseWrapper.getAllByteInReponse());
                throw e;
            }

            // write response log
            extraParam = extraParamsResponse(responseWrapper);

            long endTime = System.nanoTime();
            double durationTime = (endTime - startTime) / 1000000000.0;
            MDC.put(DURATION_TIME, String.format("%.2f", durationTime));

            writeResponseLog(operatorName, requestWrapper, responseWrapper, extraParam);

            response.getOutputStream().write(responseWrapper.getAllByteInReponse());
        } finally {
            cleanMDC();
        }
    }

    private void buildMDC(String clientIP, String httpMethod) {
        MDC.put(CLIENT_IP, clientIP);
        MDC.put(HTTP_METHOD, httpMethod);
    }

    private void cleanMDC() {
        MDC.remove(CLIENT_IP);
        MDC.remove(HTTP_METHOD);
        MDC.remove(DURATION_TIME);
    }

    protected void writeRequestLog(String operatorName, MyHttpServletRequestWrapper request,
                                   Map<String, String> extraParam) {
        String stepName = "request";
        String message = null;

        if (HttpMethod.GET.name().equals(request.getMethod())) {
            message = request.getQueryString();
        } else {
            if (request.getContentLength() > MAX_CONTENT_LENGTH_REQUEST_LOG) {
                message = new String(request.getAllByteInRequest());
                if (message.length() > MAX_CONTENT_LENGTH_REQUEST_LOG) {
                    message = message.substring(0, MAX_CONTENT_LENGTH_REQUEST_LOG);
                }
            }
            else {
                message = new String(request.getAllByteInRequest());
            }
        }

        LoggerUtils2.info(this.getClass(), operatorName, stepName,  message, extraParam);
    }

    protected void writeResponseLog(String operatorName, MyHttpServletRequestWrapper request,
                                    MyHttpServletReponseWrapper response, Map<String, String> extraParam) {
        String stepName = "response";

        String message = null;
        if (response.size() > MAX_CONTENT_LENGTH_RESPONSE_LOG) {
            message = new String(response.getAllByteInReponse());
            if (message.length() > MAX_CONTENT_LENGTH_RESPONSE_LOG) {
//                message = new String(response.getAllByteInReponse(), 0, MAX_CONTENT_LENGTH_RESPONSE_LOG);
                message = message.substring(0, MAX_CONTENT_LENGTH_RESPONSE_LOG);
            }
        } else
            message = new String(response.getAllByteInReponse());
            LoggerUtils2.info(this.getClass(), operatorName, stepName,  message, extraParam);
    }

    protected void writeErrorLog(String operatorName, MyHttpServletRequestWrapper request,
                                 MyHttpServletReponseWrapper response, Exception ex) {
        String stepName = "exception";

        LoggerUtils2.error(this.getClass(), operatorName, stepName,  ex);
    }

    protected Map<String, String> extraParams(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        Map<String, String> extraParams = new LinkedHashMap<>();

        for (String name = null; headers.hasMoreElements();) {
            name = headers.nextElement();
            extraParams.put("header." + name, request.getHeader(name));
        }

        extraParams.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, request.getRemoteHost());
        extraParams.put(ClassicConstants.REQUEST_REQUEST_URI, request.getRequestURI());
        StringBuffer requestURL = request.getRequestURL();
        if (requestURL != null) {
            extraParams.put(ClassicConstants.REQUEST_REQUEST_URL, requestURL.toString());
        }
        extraParams.put(ClassicConstants.REQUEST_METHOD, request.getMethod());
        extraParams.put(ClassicConstants.REQUEST_QUERY_STRING, request.getQueryString());
        extraParams.put(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, request.getHeader("User-Agent"));
        extraParams.put(ClassicConstants.REQUEST_X_FORWARDED_FOR, request.getHeader("X-Forwarded-For"));

        return extraParams;

    }

    protected Map<String, String> extraParamsResponse(HttpServletResponse response) {
        Collection<String> headers = response.getHeaderNames();
        Map<String, String> extraParams = new LinkedHashMap<>();

        headers.forEach(h -> {
            extraParams.put("header." + h, response.getHeader(h));
        });

        extraParams.put("header.status", String.valueOf(response.getStatus()));

        return extraParams;

    }

}