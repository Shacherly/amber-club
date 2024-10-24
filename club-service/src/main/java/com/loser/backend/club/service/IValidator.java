package com.loser.backend.club.service;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;

public interface IValidator {

    void checkMultiLang(JSONObject jsonObject, Field field);

    void checkMultiPlatform(JSONObject jsonObject);
}
