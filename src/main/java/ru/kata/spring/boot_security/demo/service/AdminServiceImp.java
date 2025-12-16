package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;

    private final SharedService sharedService;


    public AdminServiceImp(UserRepository userRepository, SharedService sharedService) {
        this.userRepository = userRepository;
        this.sharedService = sharedService;

    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user, List<Long> role, String password) {
        userRepository.save(sharedService.updateUser(user, role, password));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
