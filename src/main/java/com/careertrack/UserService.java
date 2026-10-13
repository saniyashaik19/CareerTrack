
package com.careertrack;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.careertrack.model.User;
import com.careertrack.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );

        return userRepository.save(user);
    }

public User loginUser(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                new RuntimeException("Invalid email or password"));

    if (!passwordEncoder.matches(
            request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid email or password");
    }

    return user;
}

}
