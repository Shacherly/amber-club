package com.loser.backend.club;


import com.google.common.collect.Lists;
import com.loser.backend.club.config.ClubConfig;
import com.loser.backend.club.mapper.ClubLevelMapper;
import com.loser.backend.club.mapper.loserManagerMapper;
import com.loser.backend.club.pojo.ClientMapping;
import com.loser.backend.club.pojo.loserClub;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;


@ActiveProfiles("localsit")
public class loserClubTests extends ClubServiceApplicationTests {

    @Autowired
    private ClubLevelMapper clubLevelMapper;
    @Autowired
    private ClubConfig clubConfig;
    @Autowired
    private loserManagerMapper managerMapper;


    @Test
    public void testDao() {
        loserClub loserClub = clubLevelMapper.getloserClub("123");
        List<loserClub> loserClubs = clubLevelMapper.getloserClubs(Lists.newArrayList("zehk.ui"));

    }

    @Test
    public void testConf() {
        LocalDateTime dateTime = clubConfig.nextClubUntil();

    }

    @Test
    public void testManager() {
        List<ClientMapping> mappings = managerMapper.getMappings(Lists.newArrayList("618f2cefebcd7c479b305047", "619623964e9bd231de0383e8", "61655cd194effc6022862d74"));
        System.out.println();
    }
}
