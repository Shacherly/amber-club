package com.loser.backend.club.mapper;

import com.loser.backend.club.common.mapper.BaseMapper;
import com.loser.backend.club.domain.loserManager;
import com.loser.backend.club.pojo.ClientMapping;

import java.util.Collection;
import java.util.List;

public interface loserManagerMapper extends BaseMapper<loserManager> {

    loserManager getManager(String uid);

    List<ClientMapping> getMappings(Collection<String> uids);

    int upsertloserManager(loserManager manager);
}