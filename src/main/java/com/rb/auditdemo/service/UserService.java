package com.rb.auditdemo.service;

import com.rb.auditdemo.audit.AuditLog;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @AuditLog(logInfo = "'查询用户信息, user name:' + #name" )
    public String getUser(String name) {
        return "Hello, my name is: " + name;
    }

    @AuditLog(logInfo = "'新增用户信息, user name:' + #name + ', user age: ' + #age + ', user sex: ' + #sex")
    public void addUser(String name, int age, String sex) {
        System.out.println("add new user success");
    }
}
