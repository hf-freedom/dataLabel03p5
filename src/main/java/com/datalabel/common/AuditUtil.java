package com.datalabel.common;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class AuditUtil {
    
    public static void log(String operationType, Object operationData, String failReason) {
        Long userId = null;
        String ip = null;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                ip = getClientIp(request);
                HttpSession session = request.getSession(false);
                if (session != null) {
                    Object user = session.getAttribute("currentUser");
                    if (user != null) {
                        userId = (Long) user.getClass().getMethod("getId").invoke(user);
                    }
                }
            }
        } catch (Exception e) {
        }
        Date operationTime = new Date();
        log(userId, operationType, ip, operationTime, operationData, failReason);
    }
    
    public static void log(Long userId, String operationType, String ip, Date operationTime, Object operationData, String failReason) {
    }
    
    private static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            int index = xForwardedFor.indexOf(",");
            if (index != -1) {
                return xForwardedFor.substring(0, index);
            } else {
                return xForwardedFor;
            }
        }
        return request.getRemoteAddr();
    }
}
