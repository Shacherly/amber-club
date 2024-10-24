package com.loser.backend.club.kafka.message;


import lombok.Data;

import java.io.Serializable;


@Data
public class ClientRelation implements Serializable {
    private static final long serialVersionUID = -8541030529734742638L;

    private String cid;

    private String cmid;

    private Boolean delete;
}
