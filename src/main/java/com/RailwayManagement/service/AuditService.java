package com.RailwayManagement.service;

import com.RailwayManagement.entity.AuditLog;
import com.RailwayManagement.entity.User;
import com.RailwayManagement.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserService userService;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository, UserService userService) {
        this.auditLogRepository = auditLogRepository;
        this.userService = userService;
    }

    public void logAction(String action, String details) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setAction(action);
            auditLog.setDetails(details);
            auditLog.setTimestamp(LocalDateTime.now());

            // Get current user from security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && 
                !authentication.getPrincipal().equals("anonymousUser")) {
                String email = authentication.getName();
                userService.getUserByEmail(email).ifPresent(auditLog::setUser);
            }

            // Get request details
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                auditLog.setIpAddress(getClientIpAddress(request));
                auditLog.setUserAgent(request.getHeader("User-Agent"));
            } else {
                auditLog.setIpAddress("SYSTEM");
                auditLog.setUserAgent("SYSTEM");
            }

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            // Log the error but don't fail the main operation
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByUser(User user, Pageable pageable) {
        return auditLogRepository.findByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByAction(String action, Pageable pageable) {
        return auditLogRepository.findByActionContainingIgnoreCase(action, pageable);
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate, pageable);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
