package com.loser.backend.club.http;

import com.alibaba.fastjson.annotation.JSONField;
import com.loser.backend.club.util.ServletHolder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @author ~~ trading.s
 * @date 14:04 09/23/21
 */

@Setter @Getter
public class ContextHeader {

    @JSONField(name = "x-gw-user")
    private String xGwUser;

    @JSONField(name = "x-gw-requestid")
    private String xGwRequestid;

    @JSONField(name = "x-gw-client-ip")
    private String xGwClientIp;

    /**
     * Web H5 IOS Android
     */
    @JSONField(name = "client_platform")
    private String clientPlatform;

    @JSONField(name = "client_version")
    private String clientVersion;

    @JSONField(name = "client_distinct_id")
    private String clientDistinctId;

    @JSONField(name = "client_timezone")
    private String clientTimezone;

    @JSONField(name = "client_language")
    private String clientLanguage = "en-US";

    @JSONField(name = "origin_channel")
    private String originChannel;

    @JSONField(name = "spring_env")
    private String springEnv;

    @JSONField(name = "aceup_userid")
    private String aceupUserid;

    @JSONField(name = "aceup_username")
    private String aceupUsername;

    @JSONField(name = "trace-id")
    private String traceId;

    public static boolean isMockEnv() {
        String mock = ServletHolder.getHttpRequest().getHeader("mock");
        return Optional.ofNullable(mock).map(Boolean::parseBoolean).orElse(false);
    }

    public String getClientLanguage() {
        return clientLanguage;
    }

    public void setClientLanguage(String lang) {
        if (StringUtils.isBlank(lang)) return;
        this.clientLanguage = lang;
    }
}
