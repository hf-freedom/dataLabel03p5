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
import java.util.List;

@Service
public class UserLogicService {
    
    @Autowired
    private UserService userService;
    
    public Result<List<User>> list(HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            List<User> users = userService.findAll();
            AuditUtil.logSuccess(userId, OperationType.USER_LIST, ip, null);
            return Result.success(users);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.USER_LIST, ip, e.getMessage(), null);
            return Result.error("查询用户列表失败: " + e.getMessage());
        }
    }
    
    public Result<User> getById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (id == null || id <= 0) {
                String failReason = "用户ID无效";
                AuditUtil.logFail(userId, OperationType.USER_GET, ip, failReason, id);
                return Result.error(failReason);
            }
            User user = userService.findById(id);
            if (user == null) {
                String failReason = "用户不存在";
                AuditUtil.logFail(userId, OperationType.USER_GET, ip, failReason, id);
                return Result.error(failReason);
            }
            AuditUtil.logSuccess(userId, OperationType.USER_GET, ip, id);
            return Result.success(user);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.USER_GET, ip, e.getMessage(), id);
            return Result.error("查询用户失败: " + e.getMessage());
        }
    }
    
    public Result<String> save(User user, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                String failReason = "用户名不能为空";
                AuditUtil.logFail(userId, OperationType.USER_SAVE, ip, failReason, user);
                return Result.error(failReason);
            }
            User existUser = userService.findByUsername(user.getUsername());
            if (existUser != null && !existUser.getId().equals(user.getId())) {
                String failReason = "用户名已存在";
                AuditUtil.logFail(userId, OperationType.USER_SAVE, ip, failReason, user);
                return Result.error(failReason);
            }
            if (userService.save(user)) {
                AuditUtil.logSuccess(userId, OperationType.USER_SAVE, ip, user);
                return Result.success("保存成功", null);
            }
            String failReason = "保存失败";
            AuditUtil.logFail(userId, OperationType.USER_SAVE, ip, failReason, user);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.USER_SAVE, ip, e.getMessage(), user);
            return Result.error("保存用户失败: " + e.getMessage());
        }
    }
    
    public Result<String> update(User user, HttpSession session, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(session);
        String ip = AuditUtil.getIpAddress(request);
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                String failReason = "未登录";
                AuditUtil.logFail(userId, OperationType.USER_UPDATE, ip, failReason, user);
                return Result.error(401, failReason);
            }
            if (currentUser.getUserType() == 0) {
                User updateUser = userService.findById(currentUser.getId());
                updateUser.setRealName(user.getRealName());
                updateUser.setEmail(user.getEmail());
                updateUser.setPhone(user.getPhone());
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    updateUser.setPassword(user.getPassword());
                }
                if (userService.update(updateUser)) {
                    session.setAttribute("currentUser", updateUser);
                    AuditUtil.logSuccess(userId, OperationType.USER_UPDATE, ip, user);
                    return Result.success("修改成功", null);
                }
            } else {
                if (userService.update(user)) {
                    AuditUtil.logSuccess(userId, OperationType.USER_UPDATE, ip, user);
                    return Result.success("修改成功", null);
                }
            }
            String failReason = "修改失败";
            AuditUtil.logFail(userId, OperationType.USER_UPDATE, ip, failReason, user);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.USER_UPDATE, ip, e.getMessage(), user);
            return Result.error("修改用户失败: " + e.getMessage());
        }
    }
    
    public Result<String> delete(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (id == null || id <= 0) {
                String failReason = "用户ID无效";
                AuditUtil.logFail(userId, OperationType.USER_DELETE, ip, failReason, id);
                return Result.error(failReason);
            }
            if (userService.deleteById(id)) {
                AuditUtil.logSuccess(userId, OperationType.USER_DELETE, ip, id);
                return Result.success("删除成功", null);
            }
            String failReason = "删除失败";
            AuditUtil.logFail(userId, OperationType.USER_DELETE, ip, failReason, id);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.USER_DELETE, ip, e.getMessage(), id);
            return Result.error("删除用户失败: " + e.getMessage());
        }
    }
    
    public Result<String> bindRole(Long userIdParam, Long roleId, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (userIdParam == null || userIdParam <= 0) {
                String failReason = "用户ID无效";
                AuditUtil.logFail(userId, OperationType.USER_BIND_ROLE, ip, failReason, 
                    "userId=" + userIdParam + ", roleId=" + roleId);
                return Result.error(failReason);
            }
            if (roleId == null || roleId <= 0) {
                String failReason = "角色ID无效";
                AuditUtil.logFail(userId, OperationType.USER_BIND_ROLE, ip, failReason, 
                    "userId=" + userIdParam + ", roleId=" + roleId);
                return Result.error(failReason);
            }
            if (userService.bindRole(userIdParam, roleId)) {
                AuditUtil.logSuccess(userId, OperationType.USER_BIND_ROLE, ip, 
                    "userId=" + userIdParam + ", roleId=" + roleId);
                return Result.success("绑定成功", null);
            }
            String failReason = "绑定失败";
            AuditUtil.logFail(userId, OperationType.USER_BIND_ROLE, ip, failReason, 
                "userId=" + userIdParam + ", roleId=" + roleId);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.USER_BIND_ROLE, ip, e.getMessage(), 
                "userId=" + userIdParam + ", roleId=" + roleId);
            return Result.error("绑定角色失败: " + e.getMessage());
        }
    }
    
    public Result<String> bindOrganization(Long userIdParam, Long orgId, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (userIdParam == null || userIdParam <= 0) {
                String failReason = "用户ID无效";
                AuditUtil.logFail(userId, OperationType.USER_BIND_ORG, ip, failReason, 
                    "userId=" + userIdParam + ", orgId=" + orgId);
                return Result.error(failReason);
            }
            if (orgId == null || orgId <= 0) {
                String failReason = "组织机构ID无效";
                AuditUtil.logFail(userId, OperationType.USER_BIND_ORG, ip, failReason, 
                    "userId=" + userIdParam + ", orgId=" + orgId);
                return Result.error(failReason);
            }
            if (userService.bindOrganization(userIdParam, orgId)) {
                AuditUtil.logSuccess(userId, OperationType.USER_BIND_ORG, ip, 
                    "userId=" + userIdParam + ", orgId=" + orgId);
                return Result.success("绑定成功", null);
            }
            String failReason = "绑定失败";
            AuditUtil.logFail(userId, OperationType.USER_BIND_ORG, ip, failReason, 
                "userId=" + userIdParam + ", orgId=" + orgId);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.USER_BIND_ORG, ip, e.getMessage(), 
                "userId=" + userIdParam + ", orgId=" + orgId);
            return Result.error("绑定组织失败: " + e.getMessage());
        }
    }
}
