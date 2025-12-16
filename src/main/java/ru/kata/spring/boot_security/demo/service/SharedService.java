package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface SharedService {
    void seedData();

    List<Role> findAllRoles();

    User findUserById(Long id);

    User updateUser(User user, List<Long> role, String password);
}