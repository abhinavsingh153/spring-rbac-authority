package com.codingshuttle.youtube.hospitalManagement.error;

public class TokenRefreshException extends RuntimeException{

    public TokenRefreshException(String message){
        super(message);
    }
}
