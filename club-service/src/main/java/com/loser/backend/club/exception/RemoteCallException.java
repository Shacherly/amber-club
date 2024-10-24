package com.loser.backend.club.exception;


import lombok.Getter;

@Getter
public class RemoteCallException extends RuntimeException {

    private static final long serialVersionUID = 471786822421876369L;

    private final ExceptionEnum exceptionEnum;

    private final Integer status;

    public RemoteCallException(Integer status, String cause, ExceptionEnum exceptionEnum) {
        super(cause);
        this.status = status;
        this.exceptionEnum = exceptionEnum;
    }
}
