package com.datalabel.logicService;

import com.datalabel.common.AuditUtil;
import com.datalabel.common.Result;
import com.datalabel.entity.User;
import com.datalabel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserLogicService {
    
    @Autowired
    private UserService userService;
    
    public Result<List<User>> list() {
        try {
            List<User> users = userService.findAll();
            AuditUtil.log("USER_LIST", null, null);
            return Result.success(users);
        } catch (Exception e) {
            AuditUtil.log("USER_LIST", null, e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    public Result<User> getById(Long id) {
        try {
            if (id == null) {
                AuditUtil.log("USER_GET_BY_ID", id, "参数id为空");
                return Result.error("参数错误");
            }
            User user = userService.findById(id);
            if (user == null) {
                AuditUtil.log("USER_GET_BY_ID", id, "用户不存在");
                return Result.error("用户不存在");
            }
            AuditUtil.log("USER_GET_BY_ID", id, null);
            return Result.success(user);
        } catch (Exception e) {
            AuditUtil.log("USER_GET_BY_ID", id, e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    public Result<String> save(User user) {
        try {
            if (user == null) {
                AuditUtil.log("USER_SAVE", user, "参数为空");
                return Result.error("参数错误");
            }
            User existUser = userService.findByUsername(user.getUsername());
            if (existUser != null && !existUser.getId().equals(user.getId())) {
                AuditUtil.log("USER_SAVE", user, "用户名已存在");
                return Result.error("用户名已存在");
            }
            boolean result = userService.save(user);
            if (result) {
                AuditUtil.log("USER_SAVE", user, null);
                return Result.success("保存成功", null);
            } else {
                AuditUtil.log("USER_SAVE", user, "保存失败");
                return Result.error("保存失败");
            }
        } catch (Exception e) {
            AuditUtil.log("USER_SAVE", user, e.getMessage());
            return Result.error("保存失败: " + e.getMessage());
        }
    }
    
    public Result<String> update(User user, HttpSession session) {
        try {
            if (user == null) {
                AuditUtil.log("USER_UPDATE", user, "参数为空");
                return Result.error("参数错误");
            }
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                AuditUtil.log("USER_UPDATE", user, "未登录");
                return Result.error(401, "未登录");
            }
            if (currentUser.getUserType() == 0) {
                User updateUser = userService.findById(currentUser.getId());
                updateUser.setRealName(user.getRealName());
                updateUser.setEmail(user.getEmail());
                updateUser.setPhone(user.getPhone());
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    updateUser.setPassword(user.getPassword());
                }
                boolean result = userService.update(updateUser);
                if (result) {
                    session.setAttribute("currentUser", updateUser);
                    AuditUtil.log("USER_UPDATE", updateUser, null);
                    return Result.success("修改成功", null);
                } else {
                    AuditUtil.log("USER_UPDATE", updateUser, "修改失败");
                    return Result.error("修改失败");
                }
            } else {
                boolean result = userService.update(user);
                if (result) {
                    AuditUtil.log("USER_UPDATE", user, null);
                    return Result.success("修改成功", null);
                } else {
                    AuditUtil.log("USER_UPDATE", user, "修改失败");
                    return Result.error("修改失败");
                }
            }
        } catch (Exception e) {
            AuditUtil.log("USER_UPDATE", user, e.getMessage());
            return Result.error("修改失败: " + e.getMessage());
        }
    }
    
    public Result<String> delete(Long id) {
        try {
            if (id == null) {
                AuditUtil.log("USER_DELETE", id, "参数id为空");
                return Result.error("参数错误");
            }
            boolean result = userService.deleteById(id);
            if (result) {
                AuditUtil.log("USER_DELETE", id, null);
                return Result.success("删除成功", null);
            } else {
                AuditUtil.log("USER_DELETE", id, "删除失败");
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            AuditUtil.log("USER_DELETE", id, e.getMessage());
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    
    public Result<String> bindRole(Long userId, Long roleId) {
        try {
            if (userId == null || roleId == null) {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                data.put("roleId", roleId);
                AuditUtil.log("USER_BIND_ROLE", data, "参数为空");
                return Result.error("参数错误");
            }
            boolean result = userService.bindRole(userId, roleId);
            if (result) {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                data.put("roleId", roleId);
                AuditUtil.log("USER_BIND_ROLE", data, null);
                return Result.success("绑定成功", null);
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                data.put("roleId", roleId);
                AuditUtil.log("USER_BIND_ROLE", data, "绑定失败");
                return Result.error("绑定失败");
            }
        } catch (Exception e) {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("roleId", roleId);
            AuditUtil.log("USER_BIND_ROLE", data, e.getMessage());
            return Result.error("绑定失败: " + e.getMessage());
        }
    }
    
    public Result<String> bindOrganization(Long userId, Long orgId) {
        try {
            if (userId == null || orgId == null) {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                data.put("orgId", orgId);
                AuditUtil.log("USER_BIND_ORG", data, "参数为空");
                return Result.error("参数错误");
            }
            boolean result = userService.bindOrganization(userId, orgId);
            if (result) {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                data.put("orgId", orgId);
                AuditUtil.log("USER_BIND_ORG", data, null);
                return Result.success("绑定成功", null);
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                data.put("orgId", orgId);
                AuditUtil.log("USER_BIND_ORG", data, "绑定失败");
                return Result.error("绑定失败");
            }
        } catch (Exception e) {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("orgId", orgId);
            AuditUtil.log("USER_BIND_ORG", data, e.getMessage());
            return Result.error("绑定失败: " + e.getMessage());
        }
    }
    
    public Result<User> login(String username, String password, Integer userType, HttpSession session) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("userType", userType);
            if (username == null || password == null || userType == null) {
                AuditUtil.log("USER_LOGIN", data, "参数为空");
                return Result.error("参数错误");
            }
            User user = userService.login(username, password);
            if (user == null) {
                AuditUtil.log("USER_LOGIN", data, "用户名或密码错误");
                return Result.error("用户名或密码错误");
            }
            if (!user.getUserType().equals(userType)) {
                AuditUtil.log("USER_LOGIN", data, "登录类型不匹配");
                return Result.error("登录类型不匹配");
            }
            session.setAttribute("currentUser", user);
            AuditUtil.log("USER_LOGIN", data, null);
            return Result.success("登录成功", user);
        } catch (Exception e) {
            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("userType", userType);
            AuditUtil.log("USER_LOGIN", data, e.getMessage());
            return Result.error("登录失败: " + e.getMessage());
        }
    }
}
