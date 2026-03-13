package com.datalabel.controller;

import com.datalabel.common.Result;
import com.datalabel.entity.User;
import com.datalabel.logicService.UserLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    @Autowired
    private UserLogicService userLogicService;
    
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
                              HttpSession session) {
        return userLogicService.login(username, password, userType, session);
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/main")
    public String mainPage(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getUserType() == 1) {
            return "admin/main";
        } else {
            return "user/profile";
        }
    }
    
    @GetMapping("/current-user")
    @ResponseBody
    public Result<User> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(user);
    }
}
