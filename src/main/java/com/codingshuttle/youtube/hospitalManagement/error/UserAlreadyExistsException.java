package com.codingshuttle.youtube.hospitalManagement.error;

public class UserAlreadyExistsException extends RuntimeException{


    public UserAlreadyExistsException(String message){
        super(message);
    }

    public UserAlreadyExistsException(){
        super();
    }

}
