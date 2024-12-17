package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.config.JwtUtil;
import com.ayman.bankapp.bankingapplication.dtos.RefreshTokenResponse;
import com.ayman.bankapp.bankingapplication.entities.User;
import com.ayman.bankapp.bankingapplication.enums.Role;
import com.ayman.bankapp.bankingapplication.exceptions.CustomException;
import com.ayman.bankapp.bankingapplication.services.TokenService;
import com.ayman.bankapp.bankingapplication.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {
    private JwtDecoder jwtDecoder;
    private UserService userService;
    private JwtUtil jwtUtil;
    @Override
    public String refreshAccessToken(String refreshToken) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(refreshToken);
            String email = decodedJwt.getSubject();
            User user = userService.getUserByEmail(email);

            if (!refreshToken.equals(user.getRefreshToken())) throw  CustomException.unauthorized("Invalid refresh token");

            Instant now = Instant.now();
            String scope = user.getRoles().stream()
                    .map(Role::toString)
                    .collect(Collectors.joining(" "));

            return jwtUtil.generateAccessToken(email, user.getId(), scope, now, 60);
        } catch (JwtException | CustomException e) {
            throw CustomException.unauthorized("Invalid or expired refresh token");
        }
    }
}
