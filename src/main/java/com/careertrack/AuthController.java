
package com.careertrack;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careertrack.model.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        try {
            User user = userService.registerUser(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error);
        }
    }

@PostMapping("/login")
public ResponseEntity<?> login(
        @RequestBody LoginRequest request,HttpSession session) {

    try {
        User user = userService.loginUser(request);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }
}

@PostMapping("/logout")
public ResponseEntity<?> logout(HttpSession session) {

    session.invalidate();

    Map<String, String> response = new HashMap<>();
    response.put("message", "Logout successful");

    return ResponseEntity.ok(response);
}


}
