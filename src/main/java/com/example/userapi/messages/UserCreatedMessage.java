package com.example.userapi.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedMessage implements Serializable {
    private String username;
    private String email;
}
