package ua.tc.marketplace.util;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ua.tc.marketplace.model.dto.user.UserDto;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

//    @Autowired
//    private IUserService service;
//
//    @Autowired
//    private MessageSource messages;
//
//    @Autowired
//    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        UserDto user = event.getUser();
        String token = event.getToken();

        String recipientAddress = user.email();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/regitrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}
