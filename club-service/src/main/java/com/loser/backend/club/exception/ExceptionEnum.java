package com.loser.backend.club.exception;


import lombok.Getter;

/**
 * @author ~~ trading.s
 * @date 16:46 09/22/21
 * @desc 错误码长度8位：{2位模块错误码前缀，10-20预留，使用20-99}{6位业务自定义唯一，100xxx作为不同模块通用错误码}
 */
@Getter
public enum ExceptionEnum {

    ARGUMENT_UNVALID(39_999_999, "Unvalid args present"),

    ARGUMENT_NULL(39_999_998, "Null args [{0}] is Illegal"),

    DEFAULT_LANG_NULL(39_999_997, "Default lang en-US is null for param field [{0}]"),

    RECORD_NOT_EXIST(39_999_996, "Record not exist"),

    HTTP_HEADER_UNVALID(39_999_995, "Unvalid header [{0}] of value [{1}]"),

    ARGS_SIZE_EXCEED(39_999_994, "Size of params exceed {0}, use paging query please"),

    UNLOCALIABLE(39_999_993, "Unlocaliable filed {0}"),

    INNERNAL_ERROR(39_100_000, "Internal server error"),

    INSERT_ERROR(39_200_001, "Insert database records error"),

    RECORD_EXISTS(39_200_002, "Insert record exists"),

    POSSESS_DISABLE(39_300_001, "Possess stage unusable"),

    GIFT_CONFIG_NULL(39_300_002, "Club gift configs on level {0} are not present"),

    GIFT_COMPARE_NULL(39_300_003, "Club gift compare on subject level {0} are not present"),

    REMOTE_SERVER_ERROR(39_400_001, "Remote server error, code: {}, reason: {}."),

    INDICATOR_CONFLICT_ERROR(39_500_001, "indicator conflict error."),

    DELETE_FORBIDDEN_ERROR(39_500_002, "delete forbidden error. msg [{0}]"),

    DOWNGRADE_NOT_SUPPORTED_ERROR(39_500_003, "Downgrade not supported."),

    CONFLICT_PRIORITY_ERROR(39_500_004, "Priority conflict."),

    ;


    private final int code;

    private final String reason;

    ExceptionEnum(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }
}
