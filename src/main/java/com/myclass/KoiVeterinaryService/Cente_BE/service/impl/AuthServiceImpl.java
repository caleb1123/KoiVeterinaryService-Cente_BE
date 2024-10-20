package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.OTP;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Role;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.IntrospectRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.LoginRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.SignupRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.IntrospectResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.LoginResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.BlackTokenRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.OTPRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.RoleRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
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
import java.util.Random;
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
    private MailService emailService;

    @Autowired
    private BlackTokenRepository blackListTokenRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    ModelMapper modelMapper;
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
                .account(modelMapper.map(user, AccountDTO.class))
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
        account.setActive(false);

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
    //generate OTP
    public String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));  // Tạo OTP 6 chữ số
    }

    @Override
    public void createAndSendOTP(String email) throws MessagingException {
        String otpCode = generateOTP();
        var user = accountRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // Tạo đối tượng OTP
        OTP otp = OTP.builder()
                .email(email)
                .otp(otpCode)
                .expiryDate(Instant.now().plus(15, ChronoUnit.MINUTES))  // OTP hết hạn sau 15 phút
                .build();

        // Lưu OTP vào database
        otpRepository.save(otp);

        // Gửi OTP tới email người dùng
        emailService.sendOTPtoActiveAccount(email, otpCode, user.getFullName());
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        OTP otpEntity = otpRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.OTP_NOT_FOUND));

        if (otpEntity.isExpired()) {
            otpRepository.delete(otpEntity);
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        if (otpEntity.getOtp().equals(otp)) {
            otpRepository.delete(otpEntity);
            Account user = accountRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            user.setActive(true);
            accountRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void SendOTPChangePassword(String email) throws MessagingException {
        String otpCode = generateOTP();
        var user = accountRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // Tạo đối tượng OTP
        OTP otp = OTP.builder()
                .email(email)
                .otp(otpCode)
                .expiryDate(Instant.now().plus(15, ChronoUnit.MINUTES))  // OTP hết hạn sau 15 phút
                .build();

        // Lưu OTP vào database
        otpRepository.save(otp);

        // Gửi OTP tới email người dùng
        emailService.sendOTPtoChangePasswordAccount(email, otpCode, user.getFullName());
    }

    @Override
    public boolean verifyOTPChangePassword(String email, String otp) {
        OTP otpEntity = otpRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.OTP_NOT_FOUND));

        if (otpEntity.isExpired()) {
            otpRepository.delete(otpEntity);
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        if (otpEntity.getOtp().equals(otp)) {
            otpRepository.delete(otpEntity);
            return true;
        }

        return false;
    }

    @Override
    public Account changePassword(String email, String password) {
        Account account = accountRepository.findByEmail(email).orElse(null);
        if(account == null){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encodedPassword = passwordEncoder.encode(password);
        account.setPassword(encodedPassword);

        // Lưu thay đổi vào cơ sở dữ liệu
        accountRepository.save(account);

        return account;
    }
}