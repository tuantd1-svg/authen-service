package com.example.authenservice.controller;

import com.example.authenservice.model.VerifyPermission;
import com.example.authenservice.model.VerifyToken;
import com.example.authenservice.respository.FunctionPermissionRepository;
import com.example.authenservice.respository.dto.FunctionPermission;
import com.example.authenservice.service.UserDetailService;
import com.example.authenservice.service.UserDetailServiceImp;
import com.example.authenservice.service.UserService;
import com.example.authenservice.utils.JwtUtils;
import com.example.commonapi.model.JwtRDto;
import com.example.commonapi.model.ResultMessage;
import com.example.commonapi.model.UserLogin;
import com.example.commonapi.parameter.enumable.EMessage;
import com.example.commonapi.parameter.enumable.ETransactionStatus;
import com.example.commonapi.parameter.enumable.ErrorCode;
import com.example.commonapi.service.SequenceGeneratorService;
import com.example.loggerapi.utils.LoggerUtils2;
import com.google.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("user")
public class UserController {
    @Inject
    private AuthenticationManager authenticationManager;


    @Inject
    private SequenceGeneratorService service;
    @Inject
    private JwtUtils jwtUtils;

    @Inject
    private UserDetailService userDetailService;

    @Autowired
    private FunctionPermissionRepository functionPermissionRepository;

    @Inject
    private UserService userService;

    public UserController(AuthenticationManager authenticationManager, SequenceGeneratorService service, JwtUtils jwtUtils, UserDetailService userDetailService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.jwtUtils = jwtUtils;
        this.userDetailService = userDetailService;
        this.userService = userService;
    }

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
            return new ResultMessage<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), true, EMessage.EXECUTE, jwtRDto);
        } catch (Exception e) {
            LoggerUtils2.error(UserController.class, "login", "exception", e);
            return new ResultMessage(ErrorCode.FAILURE.getCode(), ErrorCode.FAILURE.getMessage() + " - " + e.getMessage(), false, EMessage.INTERNAL_ERROR);
        }
    }

    @RequestMapping(value = "verify-token",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON)
    public VerifyPermission verifyToken(@RequestBody VerifyToken verifyToken) {
        return userService.checkPermission(verifyToken.getUri());
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public void addPermission(@RequestBody FunctionPermission functionPermission)
    {
        functionPermission.setId(service.generateSequence(FunctionPermission.SEQUENCE_NAME));
        functionPermissionRepository.save(functionPermission);
    }

}
