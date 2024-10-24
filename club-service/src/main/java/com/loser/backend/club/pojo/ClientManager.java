package com.loser.backend.club.pojo;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter @Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ClientManager implements Serializable {
    private static final long serialVersionUID = 875870119490843799L;

    private ManagerContact contact;

}
