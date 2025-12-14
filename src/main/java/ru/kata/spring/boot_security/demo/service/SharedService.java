package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface SharedService {
    void seedData();
    User findUserById(Long id);
    User updateUser(User user, List<Long> role, String password);
}