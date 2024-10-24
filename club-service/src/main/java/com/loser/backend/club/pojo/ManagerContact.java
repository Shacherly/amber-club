package com.loser.backend.club.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.StringJoiner;

@Setter @Getter @Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ManagerContact {

    @JSONField(name = "email", ordinal = 1)
    private String email;

    @JSONField(name = "phone", ordinal = 2)
    private String phone;

    @JSONField(name = "whats_app", ordinal = 3)
    private String whatsApp;

    @JSONField(name = "telegram", ordinal = 4)
    private String telegram;


    @Override
    public String toString() {
        return new StringJoiner(", ", ManagerContact.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .add("phone='" + phone + "'")
                .add("whatsApp='" + whatsApp + "'")
                .add("telegram='" + telegram + "'")
                .toString();
    }

    public static ManagerContact mock() {
        return new ManagerContact()
                .setEmail("1@qq.com")
                .setPhone("123")
                .setWhatsApp("123")
                .setTelegram("123");
    }
}