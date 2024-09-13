package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.LoginRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.SignupRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.LoginResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "http://localhost:3001")
public class AuthController {
    @Autowired
    AuthService authService;

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(null); // Trả về null nếu có lỗi
        }
    }

    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignupRequest signupRequest) {
        try {
            boolean result = authService.register(signupRequest);
            if (result) {
                return ResponseEntity.status(201).body("Registration successful");
            } else {
                return ResponseEntity.status(400).body("Registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

}
