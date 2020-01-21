package com.mall.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class UserRegisterEvent extends ApplicationEvent {
    private String email;

    public UserRegisterEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
