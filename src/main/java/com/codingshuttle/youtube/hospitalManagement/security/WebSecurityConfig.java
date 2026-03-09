package com.codingshuttle.youtube.hospitalManagement.security;

import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.codingshuttle.youtube.hospitalManagement.entity.type.PermissionType.*;
import static com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType.ADMIN;
import static com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType.DOCTOR;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuth2SuccessHanlder oAuth2SuccessHanlder;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

            httpSecurity
                    .csrf(csrfConfig -> csrfConfig.disable())
                    .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/public/**", "/auth/**").permitAll()
                            .requestMatchers("/admin/**").hasRole(RoleType.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE, "/admin/**")
                            .hasAnyAuthority(APPOINTMENT_DELETE.name(),
                                    USER_MANAGE.name())
                            .requestMatchers("/doctors/**").hasAnyRole(DOCTOR.name(), ADMIN.name())
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .oauth2Login(oauth2 -> oauth2.failureHandler(
                            (request, response, exception) -> {
                                log.error("Oauth2 error: {}" , exception.getMessage());

                                handlerExceptionResolver.resolveException(request,response ,null , exception );

                            }
                    )
                            .successHandler(oAuth2SuccessHanlder)
                            )
                    .exceptionHandling(exceptionConifg -> exceptionConifg.accessDeniedHandler((request, response, accessDeniedException) -> {

                        handlerExceptionResolver.resolveException(request,response ,null , accessDeniedException );
                    }));
//                .formLogin(Customizer.withDefaults());
            return httpSecurity.build();

    }

}
