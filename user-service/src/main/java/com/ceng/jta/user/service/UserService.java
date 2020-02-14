package com.ceng.jta.user.service;

import com.ceng.jta.user.model.User;
import com.ceng.jta.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    public User get(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
