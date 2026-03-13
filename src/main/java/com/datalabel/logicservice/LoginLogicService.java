package com.datalabel.logicservice;

import com.datalabel.common.Result;
import com.datalabel.entity.User;
import com.datalabel.service.UserService;
import com.datalabel.util.AuditUtil;
import com.datalabel.util.OperationType;
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
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (username == null || username.trim().isEmpty()) {
                String failReason = "用户名不能为空";
                AuditUtil.logFail(null, OperationType.LOGIN, ip, failReason, 
                    "username=" + username + ", userType=" + userType);
                return Result.error(failReason);
            }
            if (password == null || password.trim().isEmpty()) {
                String failReason = "密码不能为空";
                AuditUtil.logFail(null, OperationType.LOGIN, ip, failReason, 
                    "username=" + username + ", userType=" + userType);
                return Result.error(failReason);
            }
            if (userType == null) {
                String failReason = "登录类型不能为空";
                AuditUtil.logFail(null, OperationType.LOGIN, ip, failReason, 
                    "username=" + username + ", userType=" + userType);
                return Result.error(failReason);
            }
            
            User user = userService.login(username, password);
            if (user == null) {
                String failReason = "用户名或密码错误";
                AuditUtil.logFail(null, OperationType.LOGIN, ip, failReason, 
                    "username=" + username + ", userType=" + userType);
                return Result.error(failReason);
            }
            if (!user.getUserType().equals(userType)) {
                String failReason = "登录类型不匹配";
                AuditUtil.logFail(null, OperationType.LOGIN, ip, failReason, 
                    "username=" + username + ", userType=" + userType);
                return Result.error(failReason);
            }
            
            session.setAttribute("currentUser", user);
            AuditUtil.logSuccess(user.getId(), OperationType.LOGIN, ip, 
                "username=" + username + ", userType=" + userType);
            return Result.success("登录成功", user);
        } catch (Exception e) {
            AuditUtil.logFail(null, OperationType.LOGIN, ip, e.getMessage(), 
                "username=" + username + ", userType=" + userType);
            return Result.error("登录失败: " + e.getMessage());
        }
    }
    
    public String logout(HttpSession session, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(session);
        String ip = AuditUtil.getIpAddress(request);
        try {
            AuditUtil.logSuccess(userId, OperationType.LOGOUT, ip, null);
            session.invalidate();
            return "redirect:/login";
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.LOGOUT, ip, e.getMessage(), null);
            return "redirect:/login";
        }
    }
    
    public Result<User> getCurrentUser(HttpSession session, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(session);
        String ip = AuditUtil.getIpAddress(request);
        try {
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                String failReason = "未登录";
                AuditUtil.logFail(userId, OperationType.GET_CURRENT_USER, ip, failReason, null);
                return Result.error(401, failReason);
            }
            AuditUtil.logSuccess(userId, OperationType.GET_CURRENT_USER, ip, null);
            return Result.success(user);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.GET_CURRENT_USER, ip, e.getMessage(), null);
            return Result.error("获取当前用户失败: " + e.getMessage());
        }
    }
    
    public String mainPage(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getUserType() == 1) {
            return "admin/main";
        } else {
            return "user/profile";
        }
    }
}
