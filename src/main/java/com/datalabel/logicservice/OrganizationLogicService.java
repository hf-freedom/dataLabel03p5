package com.datalabel.logicservice;

import com.datalabel.common.Result;
import com.datalabel.entity.Organization;
import com.datalabel.service.OrganizationService;
import com.datalabel.util.AuditUtil;
import com.datalabel.util.OperationType;
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
        String ip = AuditUtil.getIpAddress(request);
        try {
            List<Organization> orgs = organizationService.findAll();
            AuditUtil.logSuccess(userId, OperationType.ORG_LIST, ip, null);
            return Result.success(orgs);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ORG_LIST, ip, e.getMessage(), null);
            return Result.error("查询组织机构列表失败: " + e.getMessage());
        }
    }
    
    public Result<List<Organization>> tree(HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            List<Organization> orgs = organizationService.findAll();
            AuditUtil.logSuccess(userId, OperationType.ORG_TREE, ip, null);
            return Result.success(orgs);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ORG_TREE, ip, e.getMessage(), null);
            return Result.error("查询组织机构树失败: " + e.getMessage());
        }
    }
    
    public Result<Organization> getById(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (id == null || id <= 0) {
                String failReason = "组织机构ID无效";
                AuditUtil.logFail(userId, OperationType.ORG_GET, ip, failReason, id);
                return Result.error(failReason);
            }
            Organization org = organizationService.findById(id);
            if (org == null) {
                String failReason = "组织机构不存在";
                AuditUtil.logFail(userId, OperationType.ORG_GET, ip, failReason, id);
                return Result.error(failReason);
            }
            AuditUtil.logSuccess(userId, OperationType.ORG_GET, ip, id);
            return Result.success(org);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ORG_GET, ip, e.getMessage(), id);
            return Result.error("查询组织机构失败: " + e.getMessage());
        }
    }
    
    public Result<String> save(Organization org, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (org.getName() == null || org.getName().trim().isEmpty()) {
                String failReason = "组织机构名称不能为空";
                AuditUtil.logFail(userId, OperationType.ORG_SAVE, ip, failReason, org);
                return Result.error(failReason);
            }
            if (organizationService.save(org)) {
                AuditUtil.logSuccess(userId, OperationType.ORG_SAVE, ip, org);
                return Result.success("保存成功", null);
            }
            String failReason = "保存失败";
            AuditUtil.logFail(userId, OperationType.ORG_SAVE, ip, failReason, org);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ORG_SAVE, ip, e.getMessage(), org);
            return Result.error("保存组织机构失败: " + e.getMessage());
        }
    }
    
    public Result<String> delete(Long id, HttpServletRequest request) {
        Long userId = AuditUtil.getCurrentUserId(request.getSession());
        String ip = AuditUtil.getIpAddress(request);
        try {
            if (id == null || id <= 0) {
                String failReason = "组织机构ID无效";
                AuditUtil.logFail(userId, OperationType.ORG_DELETE, ip, failReason, id);
                return Result.error(failReason);
            }
            if (organizationService.deleteById(id)) {
                AuditUtil.logSuccess(userId, OperationType.ORG_DELETE, ip, id);
                return Result.success("删除成功", null);
            }
            String failReason = "删除失败";
            AuditUtil.logFail(userId, OperationType.ORG_DELETE, ip, failReason, id);
            return Result.error(failReason);
        } catch (Exception e) {
            AuditUtil.logFail(userId, OperationType.ORG_DELETE, ip, e.getMessage(), id);
            return Result.error("删除组织机构失败: " + e.getMessage());
        }
    }
}
