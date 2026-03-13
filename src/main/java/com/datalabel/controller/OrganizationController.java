package com.datalabel.controller;

import com.datalabel.common.Result;
import com.datalabel.entity.Organization;
import com.datalabel.logicservice.OrganizationLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/org")
public class OrganizationController {

    @Autowired
    private OrganizationLogicService organizationLogicService;

    @GetMapping("/list")
    public Result<List<Organization>> list(HttpServletRequest request) {
        return organizationLogicService.list(request);
    }

    @GetMapping("/tree")
    public Result<List<Organization>> tree(HttpServletRequest request) {
        return organizationLogicService.tree(request);
    }

    @GetMapping("/{id}")
    public Result<Organization> getById(@PathVariable Long id, HttpServletRequest request) {
        return organizationLogicService.getById(id, request);
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Organization org, HttpServletRequest request) {
        return organizationLogicService.save(org, request);
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        return organizationLogicService.deleteById(id, request);
    }
}
