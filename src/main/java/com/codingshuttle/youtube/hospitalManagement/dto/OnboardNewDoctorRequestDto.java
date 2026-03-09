package com.codingshuttle.youtube.hospitalManagement.dto;

import lombok.Data;

@Data
public class OnboardNewDoctorRequestDto {

    private Long userId;
    private String specialization;
    private String name;

}
