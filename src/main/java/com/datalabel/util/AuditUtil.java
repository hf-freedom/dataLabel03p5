package com.datalabel.util;

import com.datalabel.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuditUtil {

    public enum OperationType {
        USER_LOGIN("用户登录"),
        USER_LOGOUT("用户登出"),
        USER_QUERY("用户查询"),
        USER_CREATE("用户创建"),
        USER_UPDATE("用户更新"),
        USER_DELETE("用户删除"),
        USER_BIND_ROLE("用户绑定角色"),
        USER_BIND_ORG("用户绑定组织机构"),
        ORG_QUERY("组织机构查询"),
        ORG_CREATE("组织机构创建"),
        ORG_UPDATE("组织机构更新"),
        ORG_DELETE("组织机构删除"),
        ROLE_QUERY("角色查询"),
        ROLE_CREATE("角色创建"),
        ROLE_UPDATE("角色更新"),
        ROLE_DELETE("角色删除");

        private final String description;

        OperationType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public static void log(Long userId, OperationType operationType, String ip, String failReason, Object operationData) {
        // 用户自行实现具体逻辑
        System.out.println("审计日志 - 用户ID: " + userId + 
                ", 操作类型: " + operationType.getDescription() + 
                ", IP: " + ip + 
                ", 失败原因: " + failReason + 
                ", 操作数据: " + operationData);
    }

    public static Long getCurrentUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        User user = (User) session.getAttribute("currentUser");
        return user != null ? user.getId() : null;
    }

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
