package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@org.springframework.stereotype.Controller
public class Controller {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public Controller(UserService userService, RoleRepository roleRepository1) {

        this.userService = userService;
        this.roleRepository = roleRepository1;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal User user, Model model) {
        User us = userService.findById(user.getId());
        model.addAttribute("msg", "Страница для USER / ADMIN");
        model.addAttribute("user", us);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user";
    }

    @PostMapping("/createUser")
    public String createUser(@RequestParam(value = "firstName") String firstName,
                             @RequestParam(value = "lastName") String lastName,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "password") String password) {
        User user = userService.registerUser(firstName, lastName, email, password);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        return "redirect:/user";
    }

    @PostMapping("/update_user")
    public String updateProfile(@RequestParam(value = "id") Long id,
                                @RequestParam(value = "firstName") String firstName,
                                @RequestParam(value = "lastName") String lastName,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "roles", required = false) List<Long> roleIds,
                                Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        userService.updateUserProf(id, firstName, lastName, email, password, isAdmin ? roleIds : null);

        return isAdmin ? "redirect:/admin/user/" + id : "redirect:/user";
    }

    @GetMapping("/create")
    public String showCreateForm() {
        return "create_user";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
