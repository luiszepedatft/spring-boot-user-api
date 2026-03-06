package com.example.userapi.events;

import com.example.userapi.dto.UserDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private final UserDTO user;

    public UserCreatedEvent(Object source, UserDTO user) {
        super(source);
        this.user = user;
    }

}
