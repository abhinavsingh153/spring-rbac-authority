package com.codingshuttle.youtube.hospitalManagement.security;

import com.codingshuttle.youtube.hospitalManagement.dto.LoginRequestDto;
import com.codingshuttle.youtube.hospitalManagement.dto.LoginResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.SignupResponseDto;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user =(User) authentication.getPrincipal();

        //generate JWT token
        String token = authUtil.generateJwtAccessToken(user);
        return new LoginResponseDto(token , user.getId());
    }

    public SignupResponseDto signup(LoginRequestDto request) {

        User user =userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user!=null) {
            throw new IllegalArgumentException("User already exists");
        }

        user = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())).build());
        return new SignupResponseDto(user.getId() , user.getUsername());

    }
}
