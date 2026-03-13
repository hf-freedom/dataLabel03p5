package com.datalabel.controller;

import com.datalabel.common.Result;
import com.datalabel.entity.User;
import com.datalabel.logicservice.LoginLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    @Autowired
    private LoginLogicService loginLogicService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    @ResponseBody
    public Result<User> login(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam Integer userType,
                              HttpSession session,
                              HttpServletRequest request) {
        return loginLogicService.login(username, password, userType, session, request);
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        return loginLogicService.logout(session, request);
    }
    
    @GetMapping("/main")
    public String mainPage(HttpSession session) {
        return loginLogicService.mainPage(session);
    }
    
    @GetMapping("/current-user")
    @ResponseBody
    public Result<User> getCurrentUser(HttpSession session, HttpServletRequest request) {
        return loginLogicService.getCurrentUser(session, request);
    }
}
