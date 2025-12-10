package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
            if (userRepository.findAll().isEmpty()) {
                List<User> users = new ArrayList<>();
                users.add(new User("User1", "Lastname1", "user1@mail.ru"));
                users.add(new User("User2", "Lastname2", "user2@mail.ru"));
                users.add(new User("User3", "Lastname3", "user3@mail.ru"));
                users.add(new User("User4", "Lastname4", "user4@mail.ru"));
                for (User user : users) {
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setEnabled(true);
                    user.setRoles(Collections.singletonList(roleUser));
                }
                userRepository.saveAll(users);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUserName("user");
                user.setLastName("user");
                user.setEmail("user@gmail.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEnabled(true);
                user.setRoles(Collections.singletonList(roleUser));
                userRepository.save(user);
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUserName("admin");
                admin.setLastName("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEnabled(true);
                admin.setRoles(Arrays.asList(roleUser, roleAdmin));
                userRepository.save(admin);
            }

        };
    }
}
