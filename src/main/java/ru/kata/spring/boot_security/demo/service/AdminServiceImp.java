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
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SharedService sharedService;
    private final RoleRepository roleRepository;

    public AdminServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           SharedService sharedService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sharedService = sharedService;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(User user, List<Long> role, String password) {
        userRepository.save(sharedService.updateUser(user, role, password));
    }

    @Override
    public void saveUser(User user, List<Long> role, String password) {
        user.setPassword(!password.isEmpty() ? passwordEncoder.encode(password) : user.getPassword());
        if (role != null && !role.isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : role) {
                roles.add(roleRepository.findById(roleId).orElseThrow());
            }
            user.setRoles(roles);
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
