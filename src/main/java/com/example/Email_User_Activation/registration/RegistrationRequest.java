package com.example.Email_User_Activation.registration;

public record RegistrationRequest(
    String firstName,
    String lastName,
    String email,
    String password,
    String role
) {
    
}
