package com.datalabel.logicservice;

import com.datalabel.common.Result;
import com.datalabel.entity.Role;
import com.datalabel.service.RoleService;
import com.datalabel.util.AuditUtil;
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
        String ip = AuditUtil.getClientIp(request);
        try {
            List<Role> roles = roleService.findAll();
            AuditUtil.log(userId, AuditUtil.OperationType.ROLE_QUERY, ip, null, null);
            return Result.success(roles);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ROLE_QUERY, ip, e.getMessage(), null);
            throw e;
        }
    }

    public Result<Role> getById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (id == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_QUERY, ip, "角色ID不能为空", id);
                return Result.error("角色ID不能为空");
            }
            Role role = roleService.findById(id);
            if (role == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_QUERY, ip, "角色不存在", id);
                return Result.error("角色不存在");
            }
            AuditUtil.log(userId, AuditUtil.OperationType.ROLE_QUERY, ip, null, id);
            return Result.success(role);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ROLE_QUERY, ip, e.getMessage(), id);
            throw e;
        }
    }

    public Result<String> save(Role role, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (role == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_CREATE, ip, "角色信息不能为空", null);
                return Result.error("角色信息不能为空");
            }
            if (role.getName() == null || role.getName().trim().isEmpty()) {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_CREATE, ip, "角色名称不能为空", role);
                return Result.error("角色名称不能为空");
            }
            if (role.getCode() == null || role.getCode().trim().isEmpty()) {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_CREATE, ip, "角色编码不能为空", role);
                return Result.error("角色编码不能为空");
            }

            boolean success = roleService.save(role);
            if (success) {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_CREATE, ip, null, role);
                return Result.success("保存成功", null);
            } else {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_CREATE, ip, "保存失败", role);
                return Result.error("保存失败");
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ROLE_CREATE, ip, e.getMessage(), role);
            throw e;
        }
    }

    public Result<String> deleteById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (id == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_DELETE, ip, "角色ID不能为空", id);
                return Result.error("角色ID不能为空");
            }

            boolean success = roleService.deleteById(id);
            if (success) {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_DELETE, ip, null, id);
                return Result.success("删除成功", null);
            } else {
                AuditUtil.log(userId, AuditUtil.OperationType.ROLE_DELETE, ip, "删除失败", id);
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ROLE_DELETE, ip, e.getMessage(), id);
            throw e;
        }
    }
}
