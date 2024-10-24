package com.loser.backend.club.aspect;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.loser.backend.club.service.IValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ：trading
 * @date ：Created in 2022/2/18 15:16
 * @description：校验切面
 * @modified By：
 */

@Aspect
@Component
@Slf4j
public class ValidAspect {

    @Autowired
    private IValidator validator;

    @Pointcut("execution(public * com.trading.backend.club.controller.*.*(..))")
    public void joinPoint() {
    }

    @Before("joinPoint()")
    public void validParam(JoinPoint point) throws Exception {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        for (int index = 0; index < parameters.length; index++) {
            Parameter param = parameters[index];
            if (param.isAnnotationPresent(Valid.class)) {
                Object arg = args[index];
                for (Field field : ReflectUtil.getFields(arg.getClass())) {
                    if (field.getType() == JSONObject.class && field.isAnnotationPresent(Valid.class)) {
                        JSONObject json = (JSONObject) ReflectUtil.getFieldValue(arg,field);
                        validator.checkMultiLang(json, field);
                    }
                }
            }
        }
    }
}
