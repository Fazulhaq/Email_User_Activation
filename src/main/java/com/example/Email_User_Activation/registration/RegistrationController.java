package com.example.Email_User_Activation.registration;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Email_User_Activation.event.RegistrationCompleteEvent;
import com.example.Email_User_Activation.user.User;
import com.example.Email_User_Activation.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registerRequest, final HttpServletRequest request){
        User user = userService.registerUser(registerRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success!, please check your email to complete your registration";
    }
    
    public String applicationUrl(HttpServletRequest request){
        return "http://"+ request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
    
}
