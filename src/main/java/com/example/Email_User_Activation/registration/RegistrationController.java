package com.example.Email_User_Activation.registration;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Email_User_Activation.event.RegistrationCompleteEvent;
import com.example.Email_User_Activation.event.listener.RegistrationCompleteEventListener;
import com.example.Email_User_Activation.registration.token.VerificationToken;
import com.example.Email_User_Activation.registration.token.VerificationTokenRepository;
import com.example.Email_User_Activation.user.User;
import com.example.Email_User_Activation.user.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final HttpServletRequest servletRequest;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registerRequest, final HttpServletRequest request){
        User user = userService.registerUser(registerRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success!, please check your email to complete your registration";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken thToken = tokenRepository.findByToken(token);
        if(thToken.getUser().isEnabled()){
            return "This account has already been verified. Please, login to your account.";
        }
        String verificationResult = userService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully. Now you can login on your account.";
        }
        String url = applicationUrl(servletRequest) + "/register/resend-verification-token?token=" + token;
        return "Invalid verification link, <a href=\"" + url + "\"> Get a new Verification link. </a>";
    }
    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) throws UnsupportedEncodingException, MessagingException{
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User theUser = verificationToken.getUser();
        resendVerificationTokenEmail(theUser, applicationUrl(request), verificationToken);
        return "A new verification link has been sent to your email, please check, to activate your account";
    }
    
    private void resendVerificationTokenEmail(User theUser, String applicationUrl, VerificationToken token) throws UnsupportedEncodingException, MessagingException {
        String url = applicationUrl + "/register/verifyEmail?token=" + token.getToken();
        eventListener.sendVerificationEmail(url);
        log.info("Click the link to verify your registration : {}", url);

    }

    public String applicationUrl(HttpServletRequest request){
        return "http://"+ request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
    
}
