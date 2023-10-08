package com.example.Email_User_Activation.user;

import java.util.List;
import java.util.Optional;

import com.example.Email_User_Activation.registration.RegistrationRequest;
import com.example.Email_User_Activation.registration.token.VerificationToken;

public interface IUserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);
    String validateToken(String token);
    VerificationToken generateNewVerificationToken(String oldToken);
}
