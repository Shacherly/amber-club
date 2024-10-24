package com.loser.backend.club.config;


import com.loser.backend.club.annotation.Authentication;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AuthorityAdvisor extends AbstractPointcutAdvisor {

    @Autowired
    private AuthorityHandlerIntercepter advice;

    @Override @NonNull
    public Pointcut getPointcut() {
        return AnnotationMatchingPointcut.forMethodAnnotation(Authentication.class);
    }

    @Override @NonNull
    public Advice getAdvice() {
        return advice;
    }

    private static final long serialVersionUID = -3066375644659268014L;

}
