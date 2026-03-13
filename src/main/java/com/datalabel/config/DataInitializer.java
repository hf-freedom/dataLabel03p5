package com.datalabel.config;

import com.datalabel.cache.LocalCache;
import com.datalabel.entity.Organization;
import com.datalabel.entity.Role;
import com.datalabel.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private AdminConfig adminConfig;
    
    @Override
    public void run(String... args) throws Exception {
        LocalCache cache = LocalCache.getInstance();
        
        User admin = new User(cache.generateId(), adminConfig.getUsername(), adminConfig.getPassword(), "管理员");
        admin.setUserType(1);
        cache.put(admin.getId(), admin);
        
        Role role1 = new Role();
        role1.setId(cache.generateId());
        role1.setName("管理员");
        role1.setCode("ADMIN");
        role1.setDescription("系统管理员角色");
        cache.put(role1.getId(), role1);
        
        Role role2 = new Role();
        role2.setId(cache.generateId());
        role2.setName("普通用户");
        role2.setCode("USER");
        role2.setDescription("普通用户角色");
        cache.put(role2.getId(), role2);
        
        Organization org1 = new Organization();
        org1.setId(cache.generateId());
        org1.setName("总公司");
        org1.setCode("HQ");
        org1.setParentId(0L);
        org1.setLevel(1);
        cache.put(org1.getId(), org1);
        
        Organization org2 = new Organization();
        org2.setId(cache.generateId());
        org2.setName("技术部");
        org2.setCode("TECH");
        org2.setParentId(org1.getId());
        org2.setLevel(2);
        cache.put(org2.getId(), org2);
        
        Organization org3 = new Organization();
        org3.setId(cache.generateId());
        org3.setName("市场部");
        org3.setCode("MARKET");
        org3.setParentId(org1.getId());
        org3.setLevel(2);
        cache.put(org3.getId(), org3);
        
        System.out.println("初始化数据完成，管理员账号: " + adminConfig.getUsername());
    }
}
