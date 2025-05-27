package ua.tc.marketplace.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.context.ApplicationEvent;
import ua.tc.marketplace.model.dto.user.UserDto;


@Setter
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String token;
    //    private Locale locale;
    private UserDto user;

    public OnRegistrationCompleteEvent(UserDto user, String token) {
        super(user);
        this.user = user;
        this.token = token;
    }

}
