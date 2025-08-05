package ua.tc.marketplace.util;

//import com.resend.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.tc.marketplace.config.ApiURLs;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
  @Value("${verification.mail.baseurl}")
  private String baseUrl;

  @Value("${verification.mail.from}")
  private String from_email;

  @Value("${verification.mail.subject}")
  private String subject;

//  private final Resend resend;

  private final JavaMailSender mailSender;

  //    public MailService(@Value("${verification.resend.api.key}") String resendApiKey) {
  //        this.resend = new Resend(resendApiKey);
  //    }
  //    public void sendVerificationEmail(String to, String token) {
  //        SimpleMailMessage message = new SimpleMailMessage();
  //        message.setTo(to);
  //        message.setSubject("Verify Your Email Address");
  //        message.setText("Click the following link to verify your email: " + baseUrl +
  // "/verify-email?token=" + token);
  //        mailSender.send(message);
  //    }

  //    public void sendVerificationEmailResend(String to_email, String token) {
  //        log.info("Resend to send to {}", to_email);
  //        String message = "Click the following link to verify your email: " +
  //                baseUrl +
  //                ApiURLs.AUTH_BASE +
  //                ApiURLs.AUTH_VERIFY_EMAIL+
  //                token;
  //
  //        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
  ////                .from("onboarding@resend.dev")
  //                .from(from_email)
  //                .to(to_email)
  //                .subject(subject)
  //                .html(message)
  //                .build();
  //
  //        SendEmailResponse data = resend.emails().send(sendEmailRequest);
  //    }

  public void sendRegistrationVerificationEmail(String to_email, String token) {

    log.info("Sending registration verification email to {}", to_email);
    String message =
        "Click the following link to verify your email: "
            + baseUrl
            + ApiURLs.AUTH_BASE
            + ApiURLs.AUTH_VERIFY_EMAIL_WITH_TOKEN
            + token;
    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(to_email);
    email.setFrom(from_email);
    email.setSubject(subject);
    email.setText(message);
    mailSender.send(email);
  }

  public void sendPasswordResetEmail(String to_email, @NotNull String token) {
    log.info("Sending password reset verification email to {}", to_email);
    String message =
        "Click the following link to reset password: "
            + baseUrl
            + ApiURLs.AUTH_BASE
            + ApiURLs.AUTH_VERIFY_PASSWORD_RESET_WITH_TOKEN
            + token;
    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(to_email);
    email.setSubject(subject);
    email.setText(message);
    mailSender.send(email);
  }
}
