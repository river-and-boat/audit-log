package com.rb.auditdemo.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AuditLogConfig {
    @Bean("auditExecutor")
    public Executor getAuditExecutor() {
        ThreadPoolTaskExecutor auditExecutor = new ThreadPoolTaskExecutor();
        auditExecutor.setCorePoolSize(10);
        auditExecutor.setMaxPoolSize(50);
        auditExecutor.setQueueCapacity(200);
        auditExecutor.setKeepAliveSeconds(60);
        auditExecutor.setThreadNamePrefix("auditExecutor--");
        auditExecutor.setWaitForTasksToCompleteOnShutdown(true);
        auditExecutor.setAwaitTerminationSeconds(60);
        return auditExecutor;
    }

    @Bean
    public SpelExpressionParser getSpelExpressionParser() {
        return new SpelExpressionParser();
    }

    @Bean
    public DefaultParameterNameDiscoverer getDefaultParameterNameDiscoverer() {
        return new DefaultParameterNameDiscoverer();
    }
}
