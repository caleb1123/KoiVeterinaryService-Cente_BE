package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Role;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.IntrospectRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.LoginRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.SignupRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.IntrospectResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.LoginResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.BlackTokenRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.RoleRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${app.jwt-secret}")
    private String SIGNER_KEY;

    @Value(value = "${app.jwt-access-expiration-milliseconds}")
    private long VALID_DURATION;

    @Value("${app.jwt-refresh-expiration-milliseconds}")
    private long REFRESHABLE_DURATION;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private BlackTokenRepository blackListTokenRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Account user = accountRepository
                .findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.PASSWORD_NOT_CORRECT);

        var token = generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public boolean register(SignupRequest signUpRequest) {
        Account existingUserEmail = accountRepository.findByEmail(signUpRequest.getEmail()).orElse(null);
        Account existingUserPhone = accountRepository.findByPhone(signUpRequest.getPhone()).orElse(null);
        Account existingUserName = accountRepository.findByUserName(signUpRequest.getUserName()).orElse(null);

        if (existingUserEmail != null) {
            throw new AppException(ErrorCode.EMAIL_TAKEN);
        }
        if (existingUserPhone != null) {
            throw new AppException(ErrorCode.PHONE_TAKEN);
        }
        if (existingUserName != null) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        signUpRequest.setPassword(encodedPassword);

        Account account = modelMapper.map(signUpRequest, Account.class);

        Role userRole = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        account.setRole(userRole);

        accountRepository.save(account);

        return true;
    }



    private String generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUserName())
                .issuer("Koi.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(account))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(Account account) {
        Role role = account.getRole();
        if (role != null) {
            return role.getRoleName().name(); // Assuming roleName is an enum or string
        }
        return "";
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();


        boolean isValid = true;
        try {
            verifyToken(token,false);
        } catch (AppException | JOSEException | ParseException e){
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (blackListTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
}