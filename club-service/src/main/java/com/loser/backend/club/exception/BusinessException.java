package com.loser.backend.club.exception;


import lombok.Getter;

import java.text.MessageFormat;

/**
 * @author ~~ trading.s
 * @date 16:46 09/22/21
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -2937777581295743452L;
    private final ExceptionEnum exceptionEnum;

    public BusinessException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public BusinessException(ExceptionEnum exceptionEnum, Object... keywords) {
        super(format(exceptionEnum, keywords));
        this.exceptionEnum = exceptionEnum;
    }

    public BusinessException(String cause, ExceptionEnum exceptionEnum) {
        super(cause);
        this.exceptionEnum = exceptionEnum;
    }

    public BusinessException(Throwable cause, ExceptionEnum exceptionEnum) {
        super(cause);
        this.exceptionEnum = exceptionEnum;
    }

    public BusinessException(String message, Throwable cause, ExceptionEnum exceptionEnum) {
        super(message, cause);
        this.exceptionEnum = exceptionEnum;
    }

    private static String format(ExceptionEnum exceptionEnum, Object ... keywords) {
        String reason = exceptionEnum.getReason();
        if (reason.contains("{0}"))
            return MessageFormat.format(reason, keywords);
        return reason;
    }

}
