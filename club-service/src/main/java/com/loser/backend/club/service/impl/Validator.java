package com.loser.backend.club.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.loser.backend.club.constant.Constant;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.exception.VisibleException;
import com.loser.backend.club.service.IValidator;
import com.loser.backend.club.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Objects;


@Service @Slf4j
public class Validator implements IValidator {


    @Override
    public void checkMultiLang(JSONObject jsonObject, Field field) {
        if (StringUtils.isEmpty(jsonObject.getString(Constant.ENGLISH_LANG))){
            throw new VisibleException(ExceptionEnum.DEFAULT_LANG_NULL, ReflectionUtil.getSnakeField(field));
        }
    }

    @Override
    public void checkMultiPlatform(JSONObject jsonObject) {
        if (Objects.isNull(jsonObject) || !jsonObject.containsKey("app") || !jsonObject.containsKey("web")){
            throw new VisibleException(ExceptionEnum.ARGUMENT_UNVALID);
        }
    }
}
