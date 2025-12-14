package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SharedService sharedService;

    public UserServiceImpl(UserRepository userRepository, SharedService sharedService) {
        this.userRepository = userRepository;
        this.sharedService = sharedService;
    }


    @Override
    public void updateUserProfile(User user, String password, List<Long> role) {
        userRepository.save(sharedService.updateUser(user, role, password));
    }
}
