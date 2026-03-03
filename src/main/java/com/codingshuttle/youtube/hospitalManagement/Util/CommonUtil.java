package com.codingshuttle.youtube.hospitalManagement.Util;

import com.codingshuttle.youtube.hospitalManagement.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public static final ResponseEntity<ApiError>   handleException(Exception ex , String message , HttpStatus httpStatus){
        ApiError apiError = new ApiError(message + ex.getMessage() , httpStatus);
        return new ResponseEntity<>(apiError , apiError.getHttpStatus());
    }

}
