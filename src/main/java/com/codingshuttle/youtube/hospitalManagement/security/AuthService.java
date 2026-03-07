package com.codingshuttle.youtube.hospitalManagement.security;

import com.codingshuttle.youtube.hospitalManagement.dto.LoginRequestDto;
import com.codingshuttle.youtube.hospitalManagement.dto.LoginResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.SignupResponseDto;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.entity.type.AuthProviderType;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    //Controller method
    public SignupResponseDto signup(LoginRequestDto signupRequestDto) {

        User user = signupInternal(signupRequestDto, AuthProviderType.EMAIL, null);
        return new SignupResponseDto(user.getId() , user.getUsername());

    }

    public User signupInternal(LoginRequestDto request, AuthProviderType authProviderType , String providerId){
        User user =userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user!=null) {
            throw new IllegalArgumentException("User already exists");
        }

        user = User.builder()
                .username(request.getUsername())
                .providerType(authProviderType)
                .providerId(providerId)
                .build();

        if (authProviderType.equals(AuthProviderType.EMAIL)){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }

    /*
    fetch provider type and providerId
    save the userInfo and provider id with user
    if the user has an account? : directly login
    otherwise: first signup and the login
     */
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {

        //fetch provider type and providerId
        AuthProviderType authProviderType = authUtil.getProviderType(registrationId);

        String providerId = authUtil.determineProviderIdFromOauth2User(oAuth2User,registrationId);

        User user = userRepository.findByProviderIdAndProviderType(providerId,authProviderType).orElse(null);

        String email = oAuth2User.getAttribute("email");

        User emailUser = userRepository.findByUsername(email).orElse(null);

        if (user==null && emailUser==null){
            //signup flow:

            String username = authUtil.determineUsernamefromOAuth2User(oAuth2User,registrationId,providerId);
            user = signupInternal(new LoginRequestDto(username, null), authProviderType, providerId);

        }else if (user!=null){

            if (email!=null && !email.isBlank() && !email.equals(user.getUsername())){
                user.setUsername(email);
                userRepository.save(user);
            }
        }else{
            throw new BadCredentialsException("This email is already registered with provider "+ email);
        }

        LoginResponseDto loginResponseDto =  new LoginResponseDto(authUtil.generateJwtAccessToken(user) , user.getId());

        //save the userInfo and provder id with user

        //if the user has an account? : direcvtly login

        //otherwise: first signup and the login

        return ResponseEntity.ok(loginResponseDto);
    }
}
