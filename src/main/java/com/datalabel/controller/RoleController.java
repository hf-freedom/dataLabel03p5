package com.datalabel.controller;

import com.datalabel.common.Result;
import com.datalabel.entity.Role;
import com.datalabel.logicService.RoleLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    @Autowired
    private RoleLogicService roleLogicService;
    
    @GetMapping("/list")
    public Result<List<Role>> list() {
        return roleLogicService.list();
    }
    
    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        return roleLogicService.getById(id);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody Role role) {
        return roleLogicService.save(role);
    }
    
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return roleLogicService.delete(id);
    }
}
