package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public String adminPage(Model model) {
        model.addAttribute("msg", "Страница для USER / ADMIN");
        return "admin";
    }

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable Long id, Model model) {
        User us = userService.findById(id);
        model.addAttribute("msg", "Страница для USER / ADMIN");
        model.addAttribute("user", us);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "users";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam(value = "id") Long id,
                             @RequestParam(value = "firstName") String firstname,
                             @RequestParam(value = "lastName") String lastName,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        userService.updateUser(id, firstname, lastName, email, password, roleIds);
        return "redirect:/admin/users";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam(value = "firstName") String firstname,
                          @RequestParam(value = "lastName") String lastName,
                          @RequestParam(value = "email") String email,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        userService.saveUser(firstname, lastName, email, password, roleIds);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
