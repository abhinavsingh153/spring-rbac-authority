package com.codingshuttle.youtube.hospitalManagement.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {

    //This is a scalable nomenclature of type resource:operation eg: pateint = resource , read = operation in resource:operation

    PATIENT_READ("patient:read"),
    PATIENT_WRITE("patient:write"),
    APPOINTMENT_READ("appointment:read"),
    APPOINTMENT_WRITE("appointment:write"),
    APPOINTMENT_DELETE("appointment:delete"),
    USER_MANAGE("user:manage"),
    REPORT_VIEW("report:view");

    private final String permission;
}
