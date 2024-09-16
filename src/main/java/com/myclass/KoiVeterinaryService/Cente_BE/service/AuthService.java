package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.IntrospectRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.LoginRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.SignupRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.IntrospectResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.LoginResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
        LoginResponse login(LoginRequest loginRequest);
        boolean register(SignupRequest signUpRequest);
        public IntrospectResponse introspect(IntrospectRequest request);
        void createAndSendOTP(String email) throws MessagingException;
        boolean verifyOTP(String email, String otp);

        void SendOTPChangePassword(String email) throws MessagingException;

        boolean verifyOTPChangePassword(String email, String otp);

        Account changePassword(String email, String password);
}
