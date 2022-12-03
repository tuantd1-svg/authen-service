package com.example.authenservice.controller;

import com.example.authenservice.service.UserDetailServiceImp;
import com.example.authenservice.utils.JwtUtils;
import com.example.commonapi.model.JwtRDto;
import com.example.commonapi.model.ResultMessage;
import com.example.commonapi.model.UserLogin;
import com.example.commonapi.parameter.enumable.EMessage;
import com.example.commonapi.parameter.enumable.ETransactionStatus;
import com.example.loggerapi.utils.LoggerUtils2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("public/api")
public class TokenController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @RequestMapping(value = "login", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
    public ResultMessage login(@Valid @RequestBody UserLogin userLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailServiceImp userDetailsServices = (UserDetailServiceImp) authentication.getPrincipal();

            List<String> roles = userDetailsServices.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
            JwtRDto jwtRDto = JwtRDto.builder()
                    .id(userDetailsServices.getId())
                    .username(userDetailsServices.getUsername())
                    .email(userDetailsServices.getEmail())
                    .isActive(userDetailsServices.getIsActive())
                    .isDelete(userDetailsServices.getIsDelete())
                    .isLocked(userDetailsServices.getIsLocked())
                    .isResetPassword(userDetailsServices.getIsResetPassword())
                    .token(jwt)
                    .roles(roles)
                    .build();
            return new ResultMessage<>(ETransactionStatus.SUCCESS.getStatus(), ETransactionStatus.SUCCESS.getMessage(), true, EMessage.EXECUTE, jwtRDto);
        } catch (Exception e) {
            LoggerUtils2.error(TokenController.class, "login", "exception", e);
            return new ResultMessage(ETransactionStatus.PROCESSING_ERROR.getStatus(), ETransactionStatus.PROCESSING_ERROR.getMessage() + " - " + e.getMessage(), false, EMessage.INTERNAL_ERROR);
        }
    }

}
