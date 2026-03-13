package com.datalabel.controller;

import com.datalabel.common.Result;
import com.datalabel.entity.User;
import com.datalabel.logicService.UserLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserLogicService userLogicService;
    
    @GetMapping("/list")
    public Result<List<User>> list() {
        return userLogicService.list();
    }
    
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return userLogicService.getById(id);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody User user) {
        return userLogicService.save(user);
    }
    
    @PostMapping("/update")
    public Result<String> update(@RequestBody User user, HttpSession session) {
        return userLogicService.update(user, session);
    }
    
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return userLogicService.delete(id);
    }
    
    @PostMapping("/bindRole")
    public Result<String> bindRole(@RequestParam Long userId, @RequestParam Long roleId) {
        return userLogicService.bindRole(userId, roleId);
    }
    
    @PostMapping("/bindOrg")
    public Result<String> bindOrganization(@RequestParam Long userId, @RequestParam Long orgId) {
        return userLogicService.bindOrganization(userId, orgId);
    }
}
