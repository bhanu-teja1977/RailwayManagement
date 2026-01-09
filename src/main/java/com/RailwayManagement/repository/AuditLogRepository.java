package com.RailwayManagement.repository;

import com.RailwayManagement.entity.AuditLog;
import com.RailwayManagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByUser(User user, Pageable pageable);
    Page<AuditLog> findByActionContainingIgnoreCase(String action, Pageable pageable);
    Page<AuditLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
