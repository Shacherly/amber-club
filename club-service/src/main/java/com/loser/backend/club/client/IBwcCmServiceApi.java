package com.loser.backend.club.client;

import com.loser.backend.club.kafka.message.ClientManagerInfo;

import java.util.List;

public interface IBwcCmServiceApi {

    List<ClientManagerInfo> getAllBwcCm();

}
