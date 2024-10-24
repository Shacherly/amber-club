package com.loser.backend.club.kafka.message;


import lombok.Data;

import java.io.Serializable;
import java.util.StringJoiner;


@Data
public class ClientManagerInfo implements Serializable {
    private static final long serialVersionUID = -8541030529734742638L;


    private String cmid;

    private String uid;

    private String internalName;

    private String internalEmail;

    private String photo;

    private String bwcName;

    private String bwcEmail;

    private String bwcPhone;

    private String whatsapp;

    private String telegram;

    private String twitter;

    private String facebook;

    private String profile;

    private Boolean defaultManager;

    private Boolean delete;

    @Override
    public String toString() {
        return new StringJoiner(", ", ClientManagerInfo.class.getSimpleName() + "[", "]")
                .add("cmid='" + cmid + "'")
                .add("uid='" + uid + "'")
                .add("internalName='" + internalName + "'")
                .add("internalEmail='" + internalEmail + "'")
                .add("photo='" + photo + "'")
                .add("bwcName='" + bwcName + "'")
                .add("bwcEmail='" + bwcEmail + "'")
                .add("bwcPhone='" + bwcPhone + "'")
                .add("whatsapp='" + whatsapp + "'")
                .add("telegram='" + telegram + "'")
                .add("twitter='" + twitter + "'")
                .add("facebook='" + facebook + "'")
                .add("profile='" + profile + "'")
                .add("defaultManager=" + defaultManager)
                .add("delete=" + delete)
                .toString();
    }
}
