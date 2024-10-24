package com.loser.backend.club.util;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ~~ trading.s
 * @date 23:33 10/05/21
 */
public class ReflectionUtil {

    private static final SimpleCache<Class<?>, Set<String>> SNAKES_CACHE = new SimpleCache<>();

    private ReflectionUtil() {}

    public static Set<String> getSnakeFields(Class<?> beanClass) {
        return Optional.ofNullable(SNAKES_CACHE.get(beanClass))
                       .orElseGet(() -> {
                           Set<String> collect =
                                   ReflectUtil.getFieldMap(beanClass).values().stream()
                                              .map(ReflectionUtil::getSnakeField).collect(Collectors.toSet());
                           SNAKES_CACHE.put(beanClass, collect);
                           return collect;
                       });
    }

    public static String getSnakeField(Field field) {
        return Optional.ofNullable(field)
                       .map(f -> {
                           return Optional.ofNullable(field.getAnnotation(JSONField.class))
                                          .map(JSONField::name)
                                          .orElseGet(() -> {
                                              return Optional.ofNullable(field.getAnnotation(JsonProperty.class))
                                                             .map(JsonProperty::value)
                                                             .orElseGet(() -> StrUtil.toUnderlineCase(field.getName()));
                                          });
                       })
                       .orElse(null);

    }
}
