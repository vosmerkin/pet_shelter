package ua.tc.marketplace.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ua.tc.marketplace.model.dto.user.UserDto;

@Component
@Slf4j
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private MailService mailService;

    @Override
    public void onApplicationEvent(@NotNull OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        log.info("ConfirmRegistration - sending verification email to {}" , event.getUser().email());
        UserDto user = event.getUser();
        String token = event.getToken();

        mailService.sendVerificationEmailJavaMailSender(user.email(), token);

    }
}
