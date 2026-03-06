package com.example.userapi.listeners;

import com.example.userapi.events.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WelcomeMessageListener {
    @Async
    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        System.out.println("Welcome " + event.getUser().getUsername() + "!");
    }

}
