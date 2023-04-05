package com.example.authenservice.service;

import com.example.authenservice.context.ICurrentUser;
import com.example.authenservice.mapper.AuthMapper;
import com.example.authenservice.mapper.UserConfigMapper;
import com.example.authenservice.mapper.UserRolesMapper;
import com.example.authenservice.mapper.UsersMapper;
import com.example.authenservice.model.VerifyPermission;
import com.example.authenservice.respository.*;
import com.example.authenservice.respository.dto.*;
import com.example.commonapi.anotation.exeption.FailureException;
import com.example.commonapi.anotation.exeption.ResourceExistException;
import com.example.commonapi.model.RegisterUser;
import com.example.commonapi.model.ResultMessage;
import com.example.commonapi.model.User;
import com.example.commonapi.parameter.enumable.*;
import com.example.queuecommonapi.config.QueueConfig;
import com.example.queuecommonapi.producer.IQueueProducer;
import com.google.inject.Inject;
import org.apache.kafka.common.errors.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConfigRepository userConfigRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private UserConfigMapper userConfigMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;
    @Autowired
    private UserAuthRepository userAuthRepository;


    @Autowired
    private IQueueProducer iQueueProducer;
    @Autowired
    private ICurrentUser iCurrentUser;
    @Resource
    private FunctionPermissionRepository functionPermissionRepository;

    public ResultMessage addUser(User user) {
        try {
            Users temp = userRepository.findUsersByUsernameOrEmailOrMobileNo(user.getUsername(), user.getEmail(), user.getMobileNo());
            if (temp != null) {
                throw new FailureException(ErrorCode.USER_EXIST.getMessage(), ErrorCode.USER_EXIST);
            }

            ResultMessage result = iQueueProducer.blockingStartRPCQueue(QueueConfig.Q_CORE_CREATE_USER, user);
            if (!EMessage.EXECUTE.equals(result.getMessage())) {
                return result;
            }

            Users users = userRepository.save(usersMapper.map(user));
            UserConfig userConfig = userConfigRepository.save(userConfigMapper.map(users.getRef()));
            UserRoles userRoles = userRoleRepository.save(userRolesMapper.map(users.getRef()));
            UserAuth userAuth = userAuthRepository.save(authMapper.map(EUserAuth.ACTIVE, users.getRef()));

            RegisterUser registerUser = getRegisterUser(users, userConfig, userRoles, userAuth);
            return new ResultMessage(ETransactionStatus.SUCCESS.getStatus(), ETransactionStatus.SUCCESS.getMessage(), true, EMessage.EXECUTE, registerUser);

        } catch (FailureException e) {
            return new ResultMessage(e.getErrorCode().getCode(), e.getErrorCode().getMessage(), false, EMessage.INTERNAL_ERROR);
        } catch (Exception e) {
            return new ResultMessage(ErrorCode.FAILURE.getCode(), ErrorCode.FAILURE.getMessage(), false, EMessage.INTERNAL_ERROR);
        }
    }

    private RegisterUser getRegisterUser(Users users, UserConfig userConfig, UserRoles userRoles, UserAuth userAuth) {
        return RegisterUser.builder()
                .username(users.getUsername())
                .email(users.getEmail())
                .mobileNo(users.getMobileNo())
                .fullName(users.getFullName())
                .dob(users.getDob())
                .role(userRoles.getRole())
                .language(userConfig.getLanguage())
                .DefaultSendNotification(userConfig.getSender())
                .isActive(userAuth.getIsActive())
                .isDelete(userAuth.getIsDelete())
                .isLocked(userAuth.getIsLocked())
                .address(users.getAddress())
                .createdDate(users.getIsLoginSuccess())
                .build();
    }

    public VerifyPermission checkPermission(String uri) {
        VerifyPermission verifyPermission = new VerifyPermission();
        try {
            FunctionPermission functionPermission = functionPermissionRepository.findFunctionPermissionByUri(uri);
            if (functionPermission == null) {
                verifyPermission.setVerifyRole(EPermission.URI_NOT_ROLE);
                return verifyPermission;
            }

            Set<ERole> roles = functionPermission.getRole();
            Set<ERole> roleUser = iCurrentUser.getRole();
            if (roles == null || roles.isEmpty()) {
                verifyPermission.setVerifyRole(EPermission.URI_NOT_ROLE);
                return verifyPermission;
            }

            if (roleUser == null || roleUser.isEmpty()) {
                verifyPermission.setVerifyRole(EPermission.USER_NOT_ROLE);
                return verifyPermission;
            }

            Set<ERole> roleUserSet = new HashSet<>(roleUser);
            EPermission permission = roleUserSet.containsAll(roles) ? EPermission.ACCEPT : EPermission.ACCESS_DENIED;
            verifyPermission.setVerifyRole(permission);
        } catch (Exception e) {
            verifyPermission.setVerifyRole(EPermission.ERROR);
        }
        return verifyPermission;
    }
}
