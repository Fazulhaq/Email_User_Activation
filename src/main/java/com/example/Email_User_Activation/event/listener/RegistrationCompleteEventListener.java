package com.example.Email_User_Activation.event.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.Email_User_Activation.event.RegistrationCompleteEvent;
import com.example.Email_User_Activation.user.User;
import com.example.Email_User_Activation.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //1. Get new registered user
        User theUser = event.getUser();
        //2. Create a verification token to the user
        String verficationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser, verficationToken);
        //4. Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verficationToken;
        //5. Send the email
        log.info("Click the link to verify your registration : {}", url);
    }
    
}
