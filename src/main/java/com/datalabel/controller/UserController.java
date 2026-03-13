package com.datalabel.controller;

import com.datalabel.common.Result;
import com.datalabel.entity.User;
import com.datalabel.logicservice.UserLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserLogicService userLogicService;
    
    @GetMapping("/list")
    public Result<List<User>> list(HttpServletRequest request) {
        return userLogicService.list(request);
    }
    
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id, HttpServletRequest request) {
        return userLogicService.getById(id, request);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody User user, HttpServletRequest request) {
        return userLogicService.save(user, request);
    }
    
    @PostMapping("/update")
    public Result<String> update(@RequestBody User user, HttpSession session, 
                                  HttpServletRequest request) {
        return userLogicService.update(user, session, request);
    }
    
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        return userLogicService.delete(id, request);
    }
    
    @PostMapping("/bindRole")
    public Result<String> bindRole(@RequestParam Long userId, @RequestParam Long roleId,
                                    HttpServletRequest request) {
        return userLogicService.bindRole(userId, roleId, request);
    }
    
    @PostMapping("/bindOrg")
    public Result<String> bindOrganization(@RequestParam Long userId, @RequestParam Long orgId,
                                            HttpServletRequest request) {
        return userLogicService.bindOrganization(userId, orgId, request);
    }
}
