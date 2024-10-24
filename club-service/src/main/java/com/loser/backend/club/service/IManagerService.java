package com.loser.backend.club.service;

import com.loser.backend.club.controller.response.ManagerVO;
import com.loser.backend.club.domain.loserManager;
import com.loser.backend.club.pojo.ManagerContact;

public interface IManagerService {


    ManagerContact getManaContact(String uid);


    loserManager getManager(String uid);


    ManagerVO getManagerVo(String uid);


}
