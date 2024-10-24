package com.loser.backend.club.common.enums;

import cn.hutool.core.collection.CollectionUtil;

import java.util.Map;

public enum ReportClassEnum {

    OBSERVE("OBSERVE","行业观察"),
    RESEARCH("RESEARCH","行业研究");

    private final String classification;
    private final String alias;

    ReportClassEnum(String classification, String alias) {
        this.classification = classification;
        this.alias = alias;
    }

    private static final Map<String,String> reportClassMap = CollectionUtil.newHashMap();

    static {
        reportClassMap.put(OBSERVE.classification, OBSERVE.alias);
        reportClassMap.put(RESEARCH.classification, RESEARCH.alias);
    }

    public static Map<String,String> getReportClassMap(){
        return reportClassMap;
    }

}
