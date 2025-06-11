package ua.tc.marketplace.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import ua.tc.marketplace.model.VerificationToken;
import ua.tc.marketplace.model.dto.user.UserDto;


@Setter
@Getter
public class OnForgetPasswordEvent extends ApplicationEvent {

    private VerificationToken token;

    public OnForgetPasswordEvent(VerificationToken token) {
        super(token);
        this.token = token;
    }

}
