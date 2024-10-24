package com.loser.backend.club.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.loser.backend.club.controller.response.ManagerVO;
import com.loser.backend.club.domain.loserManager;
import com.loser.backend.club.mapper.loserManagerMapper;
import com.loser.backend.club.pojo.ManagerContact;
import com.loser.backend.club.service.IManagerService;
import com.loser.backend.club.util.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author ~~ trading.Shi
 * @date 16:28 02/16/22
 */
@Slf4j @Service
public class ManagerServiceImpl implements IManagerService {


    @Autowired
    private loserManagerMapper managerMapper;


    @Override
    public ManagerContact getManaContact(String uid) {
        loserManager manager = managerMapper.getManager(uid);
        return JSONObject.toJavaObject(manager.getloserContact(), ManagerContact.class);
    }

    @Override
    public loserManager getManager(String uid) {
        return managerMapper.getManager(uid);
    }

    @Override
    public ManagerVO getManagerVo(String uid) {
        String language = ContextHolder.get().getClientLanguage();
        loserManager mana = managerMapper.getManager(uid);
        return Optional.ofNullable(mana)
                       .map(manager -> {
                           ManagerVO vo = new ManagerVO();
                           vo.setManagerName(manager.toLocalized("loserName"));
                           vo.setManagerResume(manager.toLocalized("loserProfile"));
                           vo.setManagerPhoto(manager.getloserPhoto());
                           vo.setManagerContact(JSONObject.toJavaObject(manager.getloserContact(), ManagerContact.class));
                           return vo;
                       })
                       .orElse(ManagerVO.non());
    }
}
