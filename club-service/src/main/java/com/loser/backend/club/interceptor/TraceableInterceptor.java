package com.loser.backend.club.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import com.loser.backend.club.annotation.Traceable;
import com.loser.backend.club.constant.Constant;
import com.loser.backend.club.util.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;


/**
 * @author ~~trading
 * @date 18:30 03/29/22
 */
@Component @Slf4j
public class TraceableInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String originTrace = MDC.get(Constant.TRACE_ID);
        if (StringUtils.isBlank(originTrace)) {
            String gwReqId = Optional.ofNullable(ContextHolder.get().getXGwRequestid()).orElse(null);
            MDC.put(Constant.TRACE_ID, Optional.ofNullable(gwReqId).orElseGet(UUID.randomUUID()::toString));
        }
        Method method = invocation.getMethod();
        Traceable traceable = AnnotationUtil.getAnnotation(method, Traceable.class);
        log.info("TraceableMethod access at method [{}]", method);
        Object proceed = null;
        try {
            proceed = invocation.proceed();
        } catch (Exception e) {
            log.error("Unexpected exception occurred when invoking method: {}", method, e);
        } finally {
            log.info("TraceableMethod left at method [{}]", method);
            if (traceable.clear()) MDC.remove(Constant.TRACE_ID);
        }
        return proceed;
    }
}
