package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.LoginRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.SignupRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.LoginResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
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
    //Active Otp
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOTP(@RequestParam String email) {
        try {
            authService.createAndSendOTP(email);  // Gọi service tạo và gửi OTP
            return ResponseEntity.ok("OTP has been sent to " + email);
        } catch (AppException e) {
            return ResponseEntity.status(404).body("User not found.");
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send OTP email.");
        }
    }

    // Xác thực OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        try {
            boolean result = authService.verifyOTP(email, otp); // Gọi service xác thực OTP
            if (result) {
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.status(400).body("OTP verification failed");
            }
        } catch (AppException e) {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    //Send OTP to change password
    @PostMapping("/send-otp-change-password")
    public ResponseEntity<String> sendOTPChangePassword(@RequestParam String email) {
        try {
            authService.SendOTPChangePassword(email);  // Gọi service tạo và gửi OTP
            return ResponseEntity.ok("OTP has been sent to " + email);
        } catch (AppException e) {
            return ResponseEntity.status(404).body("User not found.");
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send OTP email.");
        }
    }

    // Xác thực OTP
    @PostMapping("/verify-otp-change-password")
    public ResponseEntity<String> verifyOTPChangePassword(@RequestParam String email, @RequestParam String otp) {
        try {
            boolean result = authService.verifyOTPChangePassword(email, otp); // Gọi service xác thực OTP
            if (result) {
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.status(400).body("OTP verification failed");
            }
        } catch (AppException e) {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    // Đổi mật khẩu
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String email, @RequestParam String password) {
        try {
            authService.changePassword(email, password); // Gọi service đổi mật khẩu
            return ResponseEntity.ok("Password changed successfully");
        } catch (AppException e) {
            return ResponseEntity.status(404).body("User not found.");
        }
    }
}
