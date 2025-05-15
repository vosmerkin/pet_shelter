package ua.tc.marketplace.util;

import com.resend.*;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.config.ApiURLs;

@Slf4j
@Service
//@RequiredArgsConstructor
public class MailService {
    @Value("${verification.mail.baseurl}")
    private String baseUrl;

//    @Value("${verification.resend_api_key}")
//    private String resendApiKey;

    @Value("${verification.mail.from}")
    private String from;

    @Value("${verification.mail.subject}")
    private String subject;


    private final Resend resend;

    public MailService(@Value("${verification.resend_api_key}") String resendApiKey) {
        this.resend = new Resend(resendApiKey);
    }
//    public void sendVerificationEmail(String to, String token) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("Verify Your Email Address");
//        message.setText("Click the following link to verify your email: " + baseUrl + "/verify-email?token=" + token);
//        mailSender.send(message);
//    }


    public void sendVerificationEmail(String to, String token) {
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html("Click the following link to verify your email: " +
                        baseUrl +
                        ApiURLs.AUTH_BASE +
                        ApiURLs.AUTH_VERIFY_EMAIL.replace("{token}", token))
                .build();

        SendEmailResponse data = resend.emails().send(sendEmailRequest);
    }
}
