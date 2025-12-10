package ru.kata.spring.boot_security.demo.service;

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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Override
    public User updateUser(long id, String firstname, String lastName, String email, String password, List<Long> roleIds) {
        User user = new User(firstname, lastName, email);
        user.setId(id);
        user.setPassword(password);
        if (!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roles.add(roleRepository.findById(roleId).orElseThrow());
            }
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User updateUserProf(long id, String firstname, String lastName, String email, String password, List<Long> role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserName(firstname);
        user.setLastName(lastName);
        user.setEmail(email);
        if (!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (role != null) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : role) {
                roles.add(roleRepository.findById(roleId).orElseThrow());
            }
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }


    @Override
    public User saveUser(String firstname, String lastName, String email, String password, List<Long> roleIds) {
        User user = new User(firstname, lastName, email);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roles.add(roleRepository.findById(roleId).orElseThrow());
            }
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User registerUser(String firstname, String lastName, String email, String password) {
        User user = new User(firstname, lastName, email);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER").orElseThrow());
        user.setRoles(roles);
        return userRepository.save(user);
    }
}
