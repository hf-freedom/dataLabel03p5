package com.datalabel.controller;

import com.datalabel.common.Result;
import com.datalabel.entity.Role;
import com.datalabel.logicservice.RoleLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    @Autowired
    private RoleLogicService roleLogicService;
    
    @GetMapping("/list")
    public Result<List<Role>> list(HttpServletRequest request) {
        return roleLogicService.list(request);
    }
    
    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id, HttpServletRequest request) {
        return roleLogicService.getById(id, request);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody Role role, HttpServletRequest request) {
        return roleLogicService.save(role, request);
    }
    
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        return roleLogicService.delete(id, request);
    }
}
