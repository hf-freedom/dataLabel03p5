package com.datalabel.logicService;

import com.datalabel.common.AuditUtil;
import com.datalabel.common.Result;
import com.datalabel.entity.Organization;
import com.datalabel.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganizationLogicService {
    
    @Autowired
    private OrganizationService organizationService;
    
    public Result<List<Organization>> list() {
        try {
            List<Organization> orgs = organizationService.findAll();
            AuditUtil.log("ORG_LIST", null, null);
            return Result.success(orgs);
        } catch (Exception e) {
            AuditUtil.log("ORG_LIST", null, e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    public Result<List<Organization>> tree() {
        try {
            List<Organization> orgs = organizationService.findAll();
            AuditUtil.log("ORG_TREE", null, null);
            return Result.success(orgs);
        } catch (Exception e) {
            AuditUtil.log("ORG_TREE", null, e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    public Result<Organization> getById(Long id) {
        try {
            if (id == null) {
                AuditUtil.log("ORG_GET_BY_ID", id, "参数id为空");
                return Result.error("参数错误");
            }
            Organization org = organizationService.findById(id);
            if (org == null) {
                AuditUtil.log("ORG_GET_BY_ID", id, "组织机构不存在");
                return Result.error("组织机构不存在");
            }
            AuditUtil.log("ORG_GET_BY_ID", id, null);
            return Result.success(org);
        } catch (Exception e) {
            AuditUtil.log("ORG_GET_BY_ID", id, e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    public Result<String> save(Organization org) {
        try {
            if (org == null) {
                AuditUtil.log("ORG_SAVE", org, "参数为空");
                return Result.error("参数错误");
            }
            boolean result = organizationService.save(org);
            if (result) {
                AuditUtil.log("ORG_SAVE", org, null);
                return Result.success("保存成功", null);
            } else {
                AuditUtil.log("ORG_SAVE", org, "保存失败");
                return Result.error("保存失败");
            }
        } catch (Exception e) {
            AuditUtil.log("ORG_SAVE", org, e.getMessage());
            return Result.error("保存失败: " + e.getMessage());
        }
    }
    
    public Result<String> delete(Long id) {
        try {
            if (id == null) {
                AuditUtil.log("ORG_DELETE", id, "参数id为空");
                return Result.error("参数错误");
            }
            boolean result = organizationService.deleteById(id);
            if (result) {
                AuditUtil.log("ORG_DELETE", id, null);
                return Result.success("删除成功", null);
            } else {
                AuditUtil.log("ORG_DELETE", id, "删除失败");
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            AuditUtil.log("ORG_DELETE", id, e.getMessage());
            return Result.error("删除失败: " + e.getMessage());
        }
    }
}
