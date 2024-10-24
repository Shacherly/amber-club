package com.loser.backend.club.pojo.sensor;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class AbstractSensorModel implements Serializable {
    private static final long serialVersionUID = 7545109350638425229L;

    @JSONField(serialize = false)
    private String uid;
}
