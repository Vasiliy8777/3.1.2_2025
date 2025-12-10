package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface AdminService {
    List<User> findAll();

    User findById(Long id);

    User updateUser(long id, String firstname, String lastName, String email, String password, List<Long> role);

    User saveUser(String firstname, String lastName, String email, String password, List<Long> role);

    void deleteById(Long id);
}
