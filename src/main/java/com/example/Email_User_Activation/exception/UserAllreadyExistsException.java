package com.example.Email_User_Activation.exception;

public class UserAllreadyExistsException extends RuntimeException {
    public UserAllreadyExistsException(String message){
        super(message);
    }
    
}
