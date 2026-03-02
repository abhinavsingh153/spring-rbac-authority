package com.codingshuttle.youtube.hospitalManagement.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String jwtToken;
    private Long userId;

//    public LoginResponseDto(String jwtToken, Long userId) {
//        this.jwtToken = jwtToken;
//        this.userId = userId;
//    }
}
