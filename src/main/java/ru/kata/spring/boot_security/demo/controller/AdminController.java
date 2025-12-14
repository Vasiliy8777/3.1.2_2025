package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.SharedService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RoleRepository roleRepository;
    private final SharedService sharedService;
    private final AdminService adminService;

    public AdminController(RoleRepository roleRepository,
                           SharedService sharedService,
                           AdminService adminService) {

        this.roleRepository = roleRepository;
        this.sharedService = sharedService;
        this.adminService = adminService;
    }

    @GetMapping("")
    public String adminPage(Model model) {
        model.addAttribute("msg", "Страница для USER / ADMIN");
        return "admin";
    }

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable Long id, Model model) {
        User us = sharedService.findUserById(id);
        model.addAttribute("msg", "Страница для USER / ADMIN");
        model.addAttribute("user", us);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = adminService.findAll();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "roles", required = false) List<Long> roleIds,
                             @RequestParam(value = "password", required = false) String password) {

        adminService.updateUser(user, roleIds, password);
        return "redirect:/admin/users";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam(value = "roles", required = false) List<Long> roleIds,
                          @RequestParam(value = "password", required = false) String password) {
        adminService.saveUser(user, roleIds, password);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) {
        adminService.deleteUserById(id);
        return "redirect:/admin/users";
    }
}
