package com.loser.backend.club.interceptor;


import com.loser.backend.club.annotation.Traceable;
import lombok.RequiredArgsConstructor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.stereotype.Component;


/**
 * @author ~~trading
 * @date 18:27 03/29/22
 */
@Component @RequiredArgsConstructor
public class TraceableAdvisor extends AbstractPointcutAdvisor {
    private static final long serialVersionUID = 1702078495853476704L;

    private final TraceableInterceptor advice;

    @Override
    public Pointcut getPointcut() {
        return AnnotationMatchingPointcut.forMethodAnnotation(Traceable.class);
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }
}
