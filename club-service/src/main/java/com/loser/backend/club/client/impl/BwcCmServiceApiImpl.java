package com.loser.backend.club.client.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.loser.backend.club.client.IBwcCmServiceApi;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.exception.RemoteCallException;
import com.loser.backend.club.kafka.message.ClientManagerInfo;
import com.loser.backend.club.util.RemoteCaller;
import com.trading.backend.coupon.http.Response;
import io.renren.commons.tools.page.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：trading
 * @date ：Created in 2022/3/4 14:26
 * @description：客戶經理服务
 * @modified By：
 */

@Service
public class BwcCmServiceApiImpl implements IBwcCmServiceApi {

    @Autowired
    private RemoteCaller caller;
    @Value("${remote-call.aceup.server.bwc-cm-list}")
    private String bwcCmList;

    @Override
    public List<ClientManagerInfo> getAllBwcCm() {
        String s = caller.authExchange(bwcCmList, null, null, HttpMethod.GET);
        Response<List<ClientManagerInfo>> pageDataResponse = JSONObject.parseObject(s, new TypeReference<Response<List<ClientManagerInfo>>>() {});
        if (pageDataResponse.getCode() == 0) {
            return pageDataResponse.getData();
        }
        throw new RemoteCallException(pageDataResponse.getCode(), pageDataResponse.getMsg(), ExceptionEnum.REMOTE_SERVER_ERROR);
    }
}
