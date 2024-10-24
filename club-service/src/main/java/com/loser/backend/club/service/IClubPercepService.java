package com.loser.backend.club.service;

import com.loser.backend.club.kafka.message.ClubUpgradeModel;

import java.util.List;

public interface IClubPercepService {


    void clubUpdateNofity(List<ClubUpgradeModel> models);
}
