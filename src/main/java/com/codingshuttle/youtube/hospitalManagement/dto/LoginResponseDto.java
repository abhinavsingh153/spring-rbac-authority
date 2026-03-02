package com.codingshuttle.youtube.hospitalManagement.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String jwtToken;
    private Long userId;
}
