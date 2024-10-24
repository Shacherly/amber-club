package com.loser.backend.club.service;

import com.loser.backend.club.controller.request.ClubLevelBindingParam;
import com.loser.backend.club.controller.response.BriefClubVO;
import com.loser.backend.club.controller.response.ClubGiftVO;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.pojo.loserClub;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface IClubService {

    ClubLevel getClubLevel(String uid);

    loserClub getloserClub(String uid);

    List<loserClub> getloserClubs(Collection<String> uids);

    Map<String, ClubLevel> getClubLevels(List<String> uids);

    BriefClubVO getBriefClub(String uid);

    List<BriefClubVO> getBriefClubs(List<String> uids);

    BriefClubVO levelUpgrade(ClubLevel origin);

    // ClubLevel upgrade(Integer newLevel, ClubLevel origin);
    //
    // ClubLevel degrade(Integer newLevel, ClubLevel origin);




    ClubGiftVO getClubGift(String uid);

    boolean upgradeRead(String uid);

    boolean upgradeAppear(String uid);


    Boolean bindRefeLevel(ClubLevelBindingParam param);

    void assetSynCallback();

    int updateByExampleSelective(ClubLevel clubLevel, Supplier<Example> supplier);

}
