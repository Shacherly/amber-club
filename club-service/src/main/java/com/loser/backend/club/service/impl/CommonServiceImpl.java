package com.loser.backend.club.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.loser.backend.club.common.enums.StatusEnum;
import com.loser.backend.club.service.ICommonService;
import com.loser.backend.club.util.ContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author ：trading
 * @date ：Created in 2022/2/22 14:08
 * @description：
 * @modified By：
 */

@Service
public class CommonServiceImpl implements ICommonService {

    @Override
    public <T> void delete(List<Long> ids, Consumer<Long> canDelete, Class<T> operateClass, UpdateByExampleSelectiveMapper<T> mapper) {

        if (CollectionUtil.isEmpty(ids)){
            return;
        }

        ids.forEach(canDelete::accept);

        T operateObj = ReflectUtil.newInstance(operateClass);
        ReflectUtil.setFieldValue(operateObj,"status", StatusEnum.DELETED.getIndicator());
        ReflectUtil.setFieldValue(operateObj,"updateBy", ContextHolder.get().getAceupUsername());
        ReflectUtil.setFieldValue(operateObj,"utime", LocalDateTime.now());

        Example query = new Example(operateClass);
        if (ids.size() == 1){
            query.createCriteria().andEqualTo("id",ids.get(0));
        }else {
            query.createCriteria().andIn("id",ids);
        }

        mapper.updateByExampleSelective(operateObj,query);
    }

    @Override
    public <T> void saveOrUpdateOnDuplicate(T param, Consumer<T> save, Consumer<T> update) {
        try {
            save.accept(param);
        }catch (DuplicateKeyException e){
            update.accept(param);
        }
    }
}
