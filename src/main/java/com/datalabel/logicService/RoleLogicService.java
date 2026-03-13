package com.datalabel.logicService;

import com.datalabel.common.AuditUtil;
import com.datalabel.common.Result;
import com.datalabel.entity.Role;
import com.datalabel.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleLogicService {
    
    @Autowired
    private RoleService roleService;
    
    public Result<List<Role>> list() {
        try {
            List<Role> roles = roleService.findAll();
            AuditUtil.log("ROLE_LIST", null, null);
            return Result.success(roles);
        } catch (Exception e) {
            AuditUtil.log("ROLE_LIST", null, e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    public Result<Role> getById(Long id) {
        try {
            if (id == null) {
                AuditUtil.log("ROLE_GET_BY_ID", id, "参数id为空");
                return Result.error("参数错误");
            }
            Role role = roleService.findById(id);
            if (role == null) {
                AuditUtil.log("ROLE_GET_BY_ID", id, "角色不存在");
                return Result.error("角色不存在");
            }
            AuditUtil.log("ROLE_GET_BY_ID", id, null);
            return Result.success(role);
        } catch (Exception e) {
            AuditUtil.log("ROLE_GET_BY_ID", id, e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    public Result<String> save(Role role) {
        try {
            if (role == null) {
                AuditUtil.log("ROLE_SAVE", role, "参数为空");
                return Result.error("参数错误");
            }
            boolean result = roleService.save(role);
            if (result) {
                AuditUtil.log("ROLE_SAVE", role, null);
                return Result.success("保存成功", null);
            } else {
                AuditUtil.log("ROLE_SAVE", role, "保存失败");
                return Result.error("保存失败");
            }
        } catch (Exception e) {
            AuditUtil.log("ROLE_SAVE", role, e.getMessage());
            return Result.error("保存失败: " + e.getMessage());
        }
    }
    
    public Result<String> delete(Long id) {
        try {
            if (id == null) {
                AuditUtil.log("ROLE_DELETE", id, "参数id为空");
                return Result.error("参数错误");
            }
            boolean result = roleService.deleteById(id);
            if (result) {
                AuditUtil.log("ROLE_DELETE", id, null);
                return Result.success("删除成功", null);
            } else {
                AuditUtil.log("ROLE_DELETE", id, "删除失败");
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            AuditUtil.log("ROLE_DELETE", id, e.getMessage());
            return Result.error("删除失败: " + e.getMessage());
        }
    }
}
