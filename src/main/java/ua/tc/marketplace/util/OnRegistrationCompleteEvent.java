package ua.tc.marketplace.util;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import ua.tc.marketplace.model.dto.user.UserDto;

@Data
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String token;
//    private Locale locale;
    private UserDto user;
    public OnRegistrationCompleteEvent(UserDto user,String token) {
        super(user);
        this.user=user;
        this.token=token;
    }

}
