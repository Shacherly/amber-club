package com.loser.backend.club.service;

import tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper;

import java.util.List;
import java.util.function.Consumer;

public interface ICommonService {

    <T> void delete(List<Long> ids, Consumer<Long> canDelete, Class<T> operateClass, UpdateByExampleSelectiveMapper<T> mapper);

    <T> void saveOrUpdateOnDuplicate(T param, Consumer<T> save, Consumer<T> update);

}
