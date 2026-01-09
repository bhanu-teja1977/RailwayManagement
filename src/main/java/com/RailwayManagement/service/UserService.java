package com.RailwayManagement.service;

import com.RailwayManagement.entity.User;
import com.RailwayManagement.exception.ResourceNotFoundException;
import com.RailwayManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }
        return userRepository.save(user);
    }
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            if (userDetails.getName() != null) user.setName(userDetails.getName());
            if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
                if (userRepository.existsByEmail(userDetails.getEmail())) {throw new IllegalStateException("Email already in use");}
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPassword() != null) {user.setPassword(passwordEncoder.encode(userDetails.getPassword()));}
            if (userDetails.getRole() != null) {user.setRole(userDetails.getRole());}
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {return userRepository.findAll();}
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) { return userRepository.findByEmail(email);}
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
    public boolean existsByEmail(String email) {return userRepository.existsByEmail(email);}
}
