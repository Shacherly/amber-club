package com.loser.backend.club.service;

import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.domain.loserMissionBinding;

import java.util.List;

public interface IMissionBindingService {

    void missionBinding(loserMission mission);

    int insertBinding(List<loserMissionBinding> records);

    int insertBinding(Long missionId, List<String> uids);
}
