package com.loser.backend.club.pojo;


import lombok.Data;

import java.io.Serializable;


@Data
public class ClientMapping implements Serializable {
    private static final long serialVersionUID = -468084179510535233L;

    private String uid;

    private String cmid;

    private String cmname;

    @Override
    public String toString() {
        return "ClientMapping{" +
                "uid='" + uid + '\'' +
                ", cmid='" + cmid + '\'' +
                ", cmname='" + cmname + '\'' +
                '}';
    }
}
