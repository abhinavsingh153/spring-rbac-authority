package com.codingshuttle.youtube.hospitalManagement.security;

import com.codingshuttle.youtube.hospitalManagement.dto.LoginResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.RefreshTokenRequestDto;
import com.codingshuttle.youtube.hospitalManagement.entity.RefreshToken;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.error.TokenRefreshException;
import com.codingshuttle.youtube.hospitalManagement.repository.RefreshTokenRepository;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthUtil authUtil;

//    private final long refreshTokenDuration = 7 * 24 * 60 * 60 * 1000; //7 days
    private final long refreshTokenDuration = 7; //7 days

    //generate refresh token
    public String generateRefreshToken(Long userId) {

        User user = userRepository.findById(userId).orElseThrow();

        RefreshToken refreshToken = RefreshToken.builder()
                .createdAt(LocalDateTime.now())
                .token(UUID.randomUUID().toString())
                .user(user)
                .active(true)
                .expiryAt(LocalDateTime.now().plusDays(refreshTokenDuration))
                .build();

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public LoginResponseDto refreshToken(RefreshTokenRequestDto request) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken()).orElseThrow();
        verifyRefreshTokenExpiry(refreshToken);
        String jwtToken = authUtil.generateJwtAccessToken(refreshToken.getUser());
        LoginResponseDto loginResponseDto = new LoginResponseDto(jwtToken , refreshToken.getToken(), refreshToken.getUser().getId());
        return loginResponseDto;
    }

    //verify expiry of refresh  token
    private RefreshToken verifyRefreshTokenExpiry(RefreshToken refreshToken) {

        if (refreshToken.getExpiryAt().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException("Refresh token expired");
        }

        return refreshToken;
    }

}
