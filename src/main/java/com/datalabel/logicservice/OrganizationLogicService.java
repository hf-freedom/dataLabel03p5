package com.datalabel.logicservice;

import com.datalabel.common.Result;
import com.datalabel.entity.Organization;
import com.datalabel.service.OrganizationService;
import com.datalabel.util.AuditUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class OrganizationLogicService {

    @Autowired
    private OrganizationService organizationService;

    public Result<List<Organization>> list(HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            List<Organization> orgs = organizationService.findAll();
            AuditUtil.log(userId, AuditUtil.OperationType.ORG_QUERY, ip, null, null);
            return Result.success(orgs);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ORG_QUERY, ip, e.getMessage(), null);
            throw e;
        }
    }

    public Result<List<Organization>> tree(HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            List<Organization> orgs = organizationService.findAll();
            AuditUtil.log(userId, AuditUtil.OperationType.ORG_QUERY, ip, null, null);
            return Result.success(orgs);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ORG_QUERY, ip, e.getMessage(), null);
            throw e;
        }
    }

    public Result<Organization> getById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (id == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_QUERY, ip, "组织机构ID不能为空", id);
                return Result.error("组织机构ID不能为空");
            }
            Organization org = organizationService.findById(id);
            if (org == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_QUERY, ip, "组织机构不存在", id);
                return Result.error("组织机构不存在");
            }
            AuditUtil.log(userId, AuditUtil.OperationType.ORG_QUERY, ip, null, id);
            return Result.success(org);
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ORG_QUERY, ip, e.getMessage(), id);
            throw e;
        }
    }

    public Result<String> save(Organization org, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (org == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_CREATE, ip, "组织机构信息不能为空", null);
                return Result.error("组织机构信息不能为空");
            }
            if (org.getName() == null || org.getName().trim().isEmpty()) {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_CREATE, ip, "组织机构名称不能为空", org);
                return Result.error("组织机构名称不能为空");
            }
            if (org.getCode() == null || org.getCode().trim().isEmpty()) {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_CREATE, ip, "组织机构编码不能为空", org);
                return Result.error("组织机构编码不能为空");
            }

            boolean success = organizationService.save(org);
            if (success) {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_CREATE, ip, null, org);
                return Result.success("保存成功", null);
            } else {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_CREATE, ip, "保存失败", org);
                return Result.error("保存失败");
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ORG_CREATE, ip, e.getMessage(), org);
            throw e;
        }
    }

    public Result<String> deleteById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getClientIp(request);
        try {
            if (id == null) {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_DELETE, ip, "组织机构ID不能为空", id);
                return Result.error("组织机构ID不能为空");
            }

            boolean success = organizationService.deleteById(id);
            if (success) {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_DELETE, ip, null, id);
                return Result.success("删除成功", null);
            } else {
                AuditUtil.log(userId, AuditUtil.OperationType.ORG_DELETE, ip, "删除失败", id);
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            AuditUtil.log(userId, AuditUtil.OperationType.ORG_DELETE, ip, e.getMessage(), id);
            throw e;
        }
    }
}
