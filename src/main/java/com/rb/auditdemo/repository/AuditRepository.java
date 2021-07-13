package com.rb.auditdemo.repository;

import com.rb.auditdemo.audit.AuditingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditingLog, Long> {
}
