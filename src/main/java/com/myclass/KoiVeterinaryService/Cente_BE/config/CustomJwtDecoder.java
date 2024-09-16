package com.myclass.KoiVeterinaryService.Cente_BE.config;


import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.IntrospectRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${app.jwt-secret}")
    private String signerKey;
    @Autowired
    AuthService authenticationService;
    private NimbusJwtDecoder nimbusJwtDecoder;
    @Override
    public Jwt decode(String token) throws JwtException {

        var response = authenticationService.introspect(
                IntrospectRequest.builder().token(token).build());

        if (!response.isValid()) throw new JwtException("Token invalid");

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
