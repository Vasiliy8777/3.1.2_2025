package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface AdminService {
    List<User> getAll();

    void saveUser(User user, List<Long> role, String password);

    void deleteUserById(Long id);
}
