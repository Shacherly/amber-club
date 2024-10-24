package com.loser.backend.club.domain.base;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.loser.backend.club.http.ContextHeader;
import com.loser.backend.club.util.ContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author ~~ trading.s
 * @date 11:31 10/06/21
 */
public interface MultiLocaliable {

    default String toLocalized(JSONObject multi) {
        if (CollectionUtil.isEmpty(multi)) return null;
        ContextHeader client = ContextHolder.get();
        String clientLang = client.getClientLanguage();
        return Optional.ofNullable(multi.getString(clientLang))
                       .filter(StringUtils::isNotBlank).orElse(multi.getString("en-US"));
    }

    default String toLocalized(String fieldName) {
        Class<?> fieldClass = ReflectUtil.getField(this.getClass(), fieldName).getType();
        if (ClassUtil.isAssignable(JSONObject.class, fieldClass)) {
            JSONObject fieldValue = (JSONObject) ReflectUtil.getFieldValue(this, fieldName);
            if (CollectionUtil.isEmpty(fieldValue)) return null;
            String clientLang = ContextHolder.get().getClientLanguage();
            return Optional.ofNullable(fieldValue.getString(clientLang))
                           .filter(StringUtils::isNotBlank).orElse(fieldValue.getString("en-US"));
        }
        throw new RuntimeException("Unlocaliable field " + fieldName + " in " + this.getClass());
    }

    default String toLocalized(Supplier<JSONObject> supplier) {
        JSONObject json = supplier.get();
        if (CollectionUtil.isEmpty(json)) return null;
        String clientLang = ContextHolder.get().getClientLanguage();
        return Optional.ofNullable(json.getString(clientLang))
                       .filter(StringUtils::isNotBlank).orElse(json.getString("en-US"));
    }
}
