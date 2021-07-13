package com.rb.auditdemo.audit;

import com.rb.auditdemo.repository.AuditRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.Executor;

@Aspect
@Component
public class AuditLogAOP {
    private final SpelExpressionParser spelExpressionParser;

    private final DefaultParameterNameDiscoverer parameterNameDiscoverer;

    private final AuditRepository auditRepository;

    private final Executor executor;

    public AuditLogAOP(
            SpelExpressionParser spelExpressionParser,
            DefaultParameterNameDiscoverer parameterNameDiscoverer,
            AuditRepository auditRepository,
            @Qualifier("auditExecutor") Executor executor
    ) {
        this.spelExpressionParser = spelExpressionParser;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        this.auditRepository = auditRepository;
        this.executor = executor;
    }

    @After(value = "@annotation(enableAuditLog)")
    public void getAuditLogInfo(JoinPoint joinPoint, AuditLog enableAuditLog) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (enableAuditLog == null) {
            enableAuditLog = signature.getMethod().getAnnotation(AuditLog.class);
        }
        AuditingLog auditingLog = AuditingLog.builder()
                .createTime(LocalDateTime.now())
                .build();
        auditingLog.setUserNickname("user-name");
        auditingLog.setInterfaceName(signature.getDeclaringTypeName() + "." + signature.getName());
        String logInfo = enableAuditLog.logInfo();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(signature.getMethod());

        if (parameterNames != null && parameterNames.length > 0) {
            EvaluationContext context = new StandardEvaluationContext();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            String operationInfo = Objects.requireNonNull(spelExpressionParser.parseExpression(logInfo).getValue(context)).toString();
            auditingLog.setOperationInfo(operationInfo);
        }
        System.out.println("Thread Id: " + Thread.currentThread().getName() + " " + auditingLog);
        executor.execute(() -> monitorSaveToDB(auditingLog));
    }

    private void monitorSaveToDB(AuditingLog auditingLog) {
        auditRepository.save(auditingLog);
        System.out.println("Thread Id: " + Thread.currentThread().getName() + " save finish");
    }
}
