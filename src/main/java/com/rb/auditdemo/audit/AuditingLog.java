package com.rb.auditdemo.audit;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
@Entity
@Table(name = "audit")
@AllArgsConstructor
@NoArgsConstructor
public class AuditingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "operation_info")
    private String operationInfo;

    @Column(name = "interface_name")
    private String interfaceName;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
