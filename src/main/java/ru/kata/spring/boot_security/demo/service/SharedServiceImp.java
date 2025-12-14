package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Transactional
public class SharedServiceImp implements SharedService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public SharedServiceImp(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @PostConstruct
    public void seedData() {
        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User("user", "user", "user@gmail.com");
            user.setPassword(passwordEncoder.encode("123"));
            user.setEnabled(true);
            user.setRoles(Collections.singletonList(roleUser));
            userRepository.save(user);
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", "admin", "admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setEnabled(true);
            admin.setRoles(Arrays.asList(roleUser, roleAdmin));
            userRepository.save(admin);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Override
    public User updateUser(User user, List<Long> role, String password) {
        User us = new User(user.getUserName(), user.getLastName(), user.getEmail());
        us.setId(user.getId());
        us.setPassword(!password.isEmpty() ? passwordEncoder.encode(password) : user.getPassword());
        if (role != null && !role.isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : role) {
                roles.add(roleRepository.findById(roleId).orElseThrow());
            }
            us.setRoles(roles);
        } else {
            us.setRoles(findUserById(user.getId()).getRoles());
        }
        return us;
    }
}
