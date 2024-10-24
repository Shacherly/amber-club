package com.loser.backend.club.common.mapper;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author ~~ trading.s
 * @date 15:33 09/21/21
 */
public interface BaseMapper<T> extends Mapper<T>, ConditionMapper<T>, InsertListMapper<T> {
}
