package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.IntrospectRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.LoginRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.SignupRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.IntrospectResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.LoginResponse;

public interface AuthService {
        LoginResponse login(LoginRequest loginRequest);
        boolean register(SignupRequest signUpRequest);
        public IntrospectResponse introspect(IntrospectRequest request);
}
