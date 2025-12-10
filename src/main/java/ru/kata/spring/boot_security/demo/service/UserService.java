package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();

    public User findById(Long id);

    User updateUser(long id, String firstname, String lastName, String email, String password, List<Long> role);

    User updateUserProf(long id, String firstname, String lastName, String email, String password, List<Long> role);

    User saveUser(String firstname, String lastName, String email, String password, List<Long> role);

    void deleteById(Long id);

    User registerUser(String firstname, String lastName, String email, String password);
}
