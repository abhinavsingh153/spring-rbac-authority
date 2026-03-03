package com.codingshuttle.youtube.hospitalManagement.error;

import com.codingshuttle.youtube.hospitalManagement.Util.CommonUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    private final CommonUtil commonUtil;

    /**
     * Hanldes UserName Not found exception - 404
     * @param ex
     * @return
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> userNameNotFoundExceptionHandler(UsernameNotFoundException ex){
        ApiError apiError = new ApiError("Username not found with username: " + ex.getMessage() , HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError , apiError.getHttpStatus());
    }



    /**
     * handle Authentication Exception - 401
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex){
        ApiError apiError = new ApiError("Authentication Failed: " + ex.getMessage() , HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError , apiError.getHttpStatus());
    }

    //handle Jwt exception -401

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex ){
        ApiError apiError = new ApiError("Invalid Jwt token : " + ex.getMessage() , HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError , apiError.getHttpStatus());
    }

    //hanlde access denied exception - 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex){
        ApiError apiError = new ApiError("Access denied: " + ex.getMessage() , HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiError , apiError.getHttpStatus());
    }

    //hanlde user already exists exception - 409
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> hanldeUserAlreadyExistsException(UserAlreadyExistsException ex){
        ApiError apiError = new ApiError("User already exists with: " + ex.getMessage() , HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus() );
    }

    //handle generic exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex){
        ApiError apiError = new ApiError("An unexpected error occurred: " + ex.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

    //hanlde Nullpointer exception
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException ex){

        log.error("Exception : NullPointerException, Line : {}, Location : {}, error : {} ",
                ex.getStackTrace()[0].getLineNumber(),
                ex.getStackTrace(), ex.getMessage());

        return CommonUtil.handleException(ex , "Null pointer" , HttpStatus.INTERNAL_SERVER_ERROR );
    }


}
