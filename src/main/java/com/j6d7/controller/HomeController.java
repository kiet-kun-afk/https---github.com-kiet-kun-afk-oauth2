package com.j6d7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.j6d7.entity.Account;
import com.j6d7.entity.Role;
import com.j6d7.repo.AccountRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @SuppressWarnings("null")
    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password) {
        accountRepository.save(Account.builder().username(username).password(passwordEncoder.encode(password))
                .role(new Role("admin", "admin")).build());
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/accessDenied")
    public String accessDenied(Model model) {
        model.addAttribute("message", "<h2 style=\"color: red;\">Access Denied</h2>");
        return "index";
    }

    @RequestMapping({ "/home/index", "home" })
    public String index(Model model) {
        model.addAttribute("message", "This is home page");
        return "index";
    }

    @RequestMapping("/home/about")
    public String about(Model model) {
        model.addAttribute("message", "This is about page");
        return "index";
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/home/admin")
    public String admin(Model model) {
        // if (!request.isUserInRole("ADMIN")) {
        // return "redirect:/accessDenied";
        // }
        model.addAttribute("message", "This is ADMIN page");
        return "index";
    }

    // @PreAuthorize("hasRole('USER')")
    @RequestMapping("/home/user")
    public String user(Model model) {
        // if (!request.isUserInRole("USER")) {
        // return "redirect:/accessDenied";
        // }
        model.addAttribute("message", "This is USER page");
        return "index";
    }

    // @PreAuthorize("isAuthenticated()")
    @RequestMapping("/home/auth")
    public String authenticated(Model model) {
        // if (request.getRemoteUser() == null) {
        // return "redirect:/accessDenied";
        // }
        model.addAttribute("message", "This is AUTHENTICATE page");
        return "index";
    }
}