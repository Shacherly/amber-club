package com.loser.backend.club.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Optional;

/**
 * @author ：trading
 * @date ：Created in 2022/2/23 11:56
 * @description：
 * @modified By：
 */

@Data
public class User {

    private String uid;

    @JSONField(name = "phone_prefix")
    private String phonePrefix;

    @JSONField(name = "phone_number")
    private String phoneNumber;

    private String email;

    @JSONField(name = "kyc_name")
    private String kycName;

    @JSONField(name = "phone_number_ds")
    private String phoneNumberDs;

    @JSONField(name = "email_ds")
    private String emailDs;

    public String getPhone(){
        return Optional.ofNullable(phoneNumber).map(v -> phonePrefix+" "+phoneNumber).orElse(null);
    }

}
