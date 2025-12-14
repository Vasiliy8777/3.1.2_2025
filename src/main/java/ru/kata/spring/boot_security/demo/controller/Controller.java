package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.SharedService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@org.springframework.stereotype.Controller
public class Controller {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final SharedService sharedService;

    public Controller(UserService userService,
                      RoleRepository roleRepository,
                      SharedService sharedService) {

        this.userService = userService;
        this.roleRepository = roleRepository;
        this.sharedService = sharedService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal User user, Model model) {
        User us = sharedService.findUserById(user.getId());
        model.addAttribute("msg", "Страница для USER / ADMIN");
        model.addAttribute("user", us);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user";
    }

    @PostMapping("/update_user")
    public String updateUserProfile(@ModelAttribute("user") User user,
                                @RequestParam(value = "roles", required = false) List<Long> roleIds,
                                @RequestParam(value = "password", required = false) String password,
                                Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        userService.updateUserProfile(user, password, isAdmin ? roleIds : null);
        return isAdmin ? "redirect:/admin/user/" + user.getId() : "redirect:/user";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
