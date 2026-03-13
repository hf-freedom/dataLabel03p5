package com.datalabel.logicservice;

import com.datalabel.common.Result;
import com.datalabel.entity.Role;
import com.datalabel.service.RoleService;
import com.datalabel.util.AuditUtil;
import com.datalabel.util.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RoleLogicService {
    
    @Autowired
    private RoleService roleService;
    
    public Result<List<Role>> list(HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            List<Role> roles = roleService.findAll();
            AuditUtil.logSuccess(userId, OperationType.ROLE_LIST, ip, null);
            return Result.success(roles);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ROLE_LIST, ip, e.getMessage(), null);
            return Result.error("查询角色列表失败: " + e.getMessage());
        }
    }
    
    public Result<Role> getById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (id == null || id <= 0) {
                String failReason = "角色ID无效";
                AuditUtil.logFail(userId, OperationType.ROLE_GET, ip, failReason, id);
                return Result.error(failReason);
            }
            Role role = roleService.findById(id);
            if (role == null) {
                String failReason = "角色不存在";
                AuditUtil.logFail(userId, OperationType.ROLE_GET, ip, failReason, id);
                return Result.error(failReason);
            }
            AuditUtil.logSuccess(userId, OperationType.ROLE_GET, ip, id);
            return Result.success(role);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ROLE_GET, ip, e.getMessage(), id);
            return Result.error("查询角色失败: " + e.getMessage());
        }
    }
    
    public Result<String> save(Role role, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (role.getName() == null || role.getName().trim().isEmpty()) {
                String failReason = "角色名称不能为空";
                AuditUtil.logFail(userId, OperationType.ROLE_SAVE, ip, failReason, role);
                return Result.error(failReason);
            }
            if (roleService.save(role)) {
                AuditUtil.logSuccess(userId, OperationType.ROLE_SAVE, ip, role);
                return Result.success("保存成功", null);
            }
            String failReason = "保存失败";
            AuditUtil.logFail(userId, OperationType.ROLE_SAVE, ip, failReason, role);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ROLE_SAVE, ip, e.getMessage(), role);
            return Result.error("保存角色失败: " + e.getMessage());
        }
    }
    
    public Result<String> delete(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (id == null || id <= 0) {
                String failReason = "角色ID无效";
                AuditUtil.logFail(userId, OperationType.ROLE_DELETE, ip, failReason, id);
                return Result.error(failReason);
            }
            if (roleService.deleteById(id)) {
                AuditUtil.logSuccess(userId, OperationType.ROLE_DELETE, ip, id);
                return Result.success("删除成功", null);
            }
            String failReason = "删除失败";
            AuditUtil.logFail(userId, OperationType.ROLE_DELETE, ip, failReason, id);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ROLE_DELETE, ip, e.getMessage(), id);
            return Result.error("删除角色失败: " + e.getMessage());
        }
    }
}
