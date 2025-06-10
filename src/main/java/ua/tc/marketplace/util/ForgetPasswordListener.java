package ua.tc.marketplace.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ForgetPasswordListener implements
        ApplicationListener<OnForgetPasswordEvent> {

    @Autowired
    private MailService mailService;

    @Override
    public void onApplicationEvent(@NotNull OnForgetPasswordEvent event) {
        this.confirmPasswordReset(event);
    }

    private  void confirmPasswordReset(OnForgetPasswordEvent event){
        log.info("ConfirmPasswordReset - sending verification email to {}" , event.getToken().getUser().getEmail());

        mailService.sendPasswordResetEmail(event.getToken().getUser().getEmail(), event.getToken().getToken());
    }
}
