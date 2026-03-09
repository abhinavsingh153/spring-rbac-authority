package com.codingshuttle.youtube.hospitalManagement.dto;

import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    private String username;
    private String password;
    private String name;

    //This is just for learning, in prod apps we must not assign roles direclty at signup like this
    private Set<RoleType> roles = new HashSet<>();
}
