package com.datalabel.logicservice;

import com.datalabel.common.Result;
import com.datalabel.entity.User;
import com.datalabel.service.UserService;
import com.datalabel.util.AuditUtil;
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
        String ip = AuditUtil.getClientIp(request);
        try {
            List<User> users = userService.findAll();
            AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, null, null);
            return Result.success(users);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, e.getMessage(), null);
            throw e;
        }
    }

    public Result<User> getById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (id == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, "用户ID不能为空", id);
                return Result.error("用户ID不能为空");
            }
            User user = userService.findById(id);
            if (user == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, "用户不存在", id);
                return Result.error("用户不存在");
            }
            AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, null, id);
            return Result.success(user);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_QUERY, ip, e.getMessage(), id);
            throw e;
        }
    }

    public Result<String> save(User user, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (user == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_CREATE, ip, "用户信息不能为空", null);
                return Result.error("用户信息不能为空");
            }
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_CREATE, ip, "用户名不能为空", user);
                return Result.error("用户名不能为空");
            }

            User existUser = userService.findByUsername(user.getUsername());
            if (existUser != null && !existUser.getId().equals(user.getId())) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_CREATE, ip, "用户名已存在", user);
                return Result.error("用户名已存在");
            }

            boolean success = userService.save(user);
            if (success) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_CREATE, ip, null, user);
                return Result.success("保存成功", null);
            } else {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_CREATE, ip, "保存失败", user);
                return Result.error("保存失败");
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_CREATE, ip, e.getMessage(), user);
            throw e;
        }
    }

    public Result<String> update(User user, HttpSession session, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_UPDATE, ip, "未登录", user);
                return Result.error(401, "未登录");
            }

            if (user == null || user.getId() == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_UPDATE, ip, "用户信息或ID不能为空", user);
                return Result.error("用户信息或ID不能为空");
            }

            if (currentUser.getUserType() == 0) {
                User updateUser = userService.findById(currentUser.getId());
                updateUser.setRealName(user.getRealName());
                updateUser.setEmail(user.getEmail());
                updateUser.setPhone(user.getPhone());
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    updateUser.setPassword(user.getPassword());
                }
                boolean success = userService.update(updateUser);
                if (success) {
                    session.setAttribute("currentUser", updateUser);
                    AuditUtil.log(userId, AuditUtil.OperationType.USER_UPDATE, ip, null, updateUser);
                    return Result.success("修改成功", null);
                } else {
                    AuditUtil.log(userId, AuditUtil.OperationType.USER_UPDATE, ip, "修改失败", updateUser);
                    return Result.error("修改失败");
                }
            } else {
                boolean success = userService.update(user);
                if (success) {
                    AuditUtil.log(userId, AuditUtil.OperationType.USER_UPDATE, ip, null, user);
                    return Result.success("修改成功", null);
                } else {
                    AuditUtil.log(userId, AuditUtil.OperationType.USER_UPDATE, ip, "修改失败", user);
                    return Result.error("修改失败");
                }
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_UPDATE, ip, e.getMessage(), user);
            throw e;
        }
    }

    public Result<String> deleteById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (id == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_DELETE, ip, "用户ID不能为空", id);
                return Result.error("用户ID不能为空");
            }

            boolean success = userService.deleteById(id);
            if (success) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_DELETE, ip, null, id);
                return Result.success("删除成功", null);
            } else {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_DELETE, ip, "删除失败", id);
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_DELETE, ip, e.getMessage(), id);
            throw e;
        }
    }

    public Result<String> bindRole(Long userIdParam, Long roleId, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        String params = "userId=" + userIdParam + ", roleId=" + roleId;
        try {
            if (userIdParam == null || roleId == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_BIND_ROLE, ip, "用户ID和角色ID不能为空", params);
                return Result.error("用户ID和角色ID不能为空");
            }

            boolean success = userService.bindRole(userIdParam, roleId);
            if (success) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_BIND_ROLE, ip, null, params);
                return Result.success("绑定成功", null);
            } else {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_BIND_ROLE, ip, "绑定失败", params);
                return Result.error("绑定失败");
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_BIND_ROLE, ip, e.getMessage(), params);
            throw e;
        }
    }

    public Result<String> bindOrganization(Long userIdParam, Long orgId, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        String params = "userId=" + userIdParam + ", orgId=" + orgId;
        try {
            if (userIdParam == null || orgId == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_BIND_ORG, ip, "用户ID和组织机构ID不能为空", params);
                return Result.error("用户ID和组织机构ID不能为空");
            }

            boolean success = userService.bindOrganization(userIdParam, orgId);
            if (success) {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_BIND_ORG, ip, null, params);
                return Result.success("绑定成功", null);
            } else {
                AuditUtil.log(userId, AuditUtil.OperationType.USER_BIND_ORG, ip, "绑定失败", params);
                return Result.error("绑定失败");
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.USER_BIND_ORG, ip, e.getMessage(), params);
            throw e;
        }
    }
}
