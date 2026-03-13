package com.datalabel.util;

import com.datalabel.entity.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuditUtil {
    
    public static void log(Long userId, String operationType, String ip, 
                          String failReason, Object operationData) {
    }
    
    public static void logSuccess(Long userId, String operationType, 
                                  String ip, Object operationData) {
        log(userId, operationType, ip, null, operationData);
    }
    
    public static void logFail(Long userId, String operationType, 
                               String ip, String failReason, Object operationData) {
        log(userId, operationType, ip, failReason, operationData);
    }
    
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public static Long getCurrentUserId(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return user != null ? user.getId() : null;
    }
}
