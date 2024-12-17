package com.ayman.bankapp.bankingapplication.config;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class JwtUtil {

    private JwtEncoder jwtEncoder;

    public String generateAccessToken(String email, String userId, String scope, Instant now, int validityMinutes) {
        JwtClaimsSet claimsBuilder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(validityMinutes, ChronoUnit.MINUTES))
                .subject(email)
                .claim("userId", userId)
                .claim("scope", scope)
                .build();

        //configuring the header
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                claimsBuilder
        );

        try {
            return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT", e);
        }
    }

    public String generateRefreshToken(String email, int validityDays) {
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(validityDays, ChronoUnit.DAYS))
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                jwtClaimsSet
        );

        try {
            return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate the refresh token", e);
        }
    }
}