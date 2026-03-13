package com.datalabel.controller;

import com.datalabel.common.Result;
import com.datalabel.entity.Organization;
import com.datalabel.logicService.OrganizationLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/org")
public class OrganizationController {
    
    @Autowired
    private OrganizationLogicService organizationLogicService;
    
    @GetMapping("/list")
    public Result<List<Organization>> list() {
        return organizationLogicService.list();
    }
    
    @GetMapping("/tree")
    public Result<List<Organization>> tree() {
        return organizationLogicService.tree();
    }
    
    @GetMapping("/{id}")
    public Result<Organization> getById(@PathVariable Long id) {
        return organizationLogicService.getById(id);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody Organization org) {
        return organizationLogicService.save(org);
    }
    
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return organizationLogicService.delete(id);
    }
}
