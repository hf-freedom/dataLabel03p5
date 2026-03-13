package com.datalabel.logicservice;

import com.datalabel.common.Result;
import com.datalabel.entity.User;
import com.datalabel.service.UserService;
import com.datalabel.util.AuditUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class LoginLogicService {

    @Autowired
    private UserService userService;

    public Result<User> login(String username, String password, Integer userType,
                              HttpSession session, HttpServletRequest request) {
        String ip = AuditUtil.getClientIp(request);
        String params = "username=" + username + ", userType=" + userType;

        try {
            if (username == null || username.trim().isEmpty()) {
                AuditUtil.log(null, AuditUtil.OperationType.USER_LOGIN, ip, "用户名不能为空", params);
                return Result.error("用户名不能为空");
            }
            if (password == null || password.isEmpty()) {
                AuditUtil.log(null, AuditUtil.OperationType.USER_LOGIN, ip, "密码不能为空", params);
                return Result.error("密码不能为空");
            }
            if (userType == null) {
                AuditUtil.log(null, AuditUtil.OperationType.USER_LOGIN, ip, "登录类型不能为空", params);
                return Result.error("登录类型不能为空");
            }

            User user = userService.login(username, password);
            if (user == null) {
                AuditUtil.log(null, AuditUtil.OperationType.USER_LOGIN, ip, "用户名或密码错误", params);
                return Result.error("用户名或密码错误");
            }
            if (!user.getUserType().equals(userType)) {
                AuditUtil.log(null, AuditUtil.OperationType.USER_LOGIN, ip, "登录类型不匹配", params);
                return Result.error("登录类型不匹配");
            }

            session.setAttribute("currentUser", user);
            AuditUtil.log(user.getId(), AuditUtil.OperationType.USER_LOGIN, ip, null, params);
            return Result.success("登录成功", user);
        } catch (Exception e) {
            AuditUtil.log(null, AuditUtil.OperationType.USER_LOGIN, ip, e.getMessage(), params);
            throw e;
        }
    }

    public void logout(HttpSession session, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(session);
        String ip = AuditUtil.getClientIp(request);
        try {
            session.invalidate();
            AuditUtil.log(userId, AuditUtil.OperationType.USER_LOGOUT, ip, null, null);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_LOGOUT, ip, e.getMessage(), null);
            throw e;
        }
    }

    public Result<User> getCurrentUser(HttpSession session, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(session);
        String ip = AuditUtil.getClientIp(request);
        try {
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, "未登录", null);
                return Result.error(401, "未登录");
            }
            AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, null, user.getId());
            return Result.success(user);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, e.getMessage(), null);
            throw e;
        }
    }
}
