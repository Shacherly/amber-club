package com.loser.backend.club.client.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loser.backend.club.client.IUserServiceApi;
import com.loser.backend.club.kafka.message.UserTouchModel;
import com.loser.backend.club.kafka.publisher.KafkaPublisher;
import com.loser.backend.club.pojo.User;
import com.loser.backend.club.util.RemoteCaller;
import org.apache.commons.lang3.StringUtils;
import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ：trading
 * @date ：Created in 2022/2/23 12:02
 * @description：
 * @modified By：
 */

@Service
public class UserServiceApiImpl implements IUserServiceApi {
    @Autowired
    private RemoteCaller caller;
    @Autowired
    private KafkaPublisher publisher;
    @Value("${remote-call.domain}")
    private String domain;
    @Value("${remote-call.user-server.user-detail}")
    private String userDetail;
    @Value("${topics.publisher.user-touch}")
    private String userTouchTopic;

    private static final String SPLIT = ",";

    @Override
    public Map<String, User> userMap(Collection<String> uids, Collection<String> phones, Collection<String> emails) {

        Map<String, User> resultMap = CollectionUtil.newHashMap();

        if (CollectionUtil.isEmpty(uids) && CollectionUtil.isEmpty(phones) && CollectionUtil.isEmpty(emails))
            return resultMap;

        String phoneStr = null, emailStr = null;
        if (CollectionUtil.isNotEmpty(phones)){
            phoneStr = CollectionUtil.join(phones,SPLIT);
        }
        if (CollectionUtil.isNotEmpty(emails)){
            emailStr = CollectionUtil.join(emails,SPLIT);
        }

        //uid too many will throw exception, so page to query
        if (CollectionUtil.isNotEmpty(uids)){
            List<List<String>> splitUidList= CollectionUtil.split(uids, 20);
            for (List<String> userList : splitUidList) {
                String uidStr = StringUtils.join(userList,SPLIT);
                resultMap.putAll(userMap(uidStr, phoneStr, emailStr));
            }
        }else {
            return userMap(null, phoneStr, emailStr);
        }

        return resultMap;

    }

    @Override
    public User userInfo(String uid) {
        return userMap(uid,null,null).get(uid);
    }

    @Override
    public void kafkaNotify(String uid, List<String> templates, Map<String, Object> templateParams, List<Integer> types, String language) {
        UserTouchModel model = new UserTouchModel();
        if (StringUtils.isNotBlank(language)) model.setLanguage(language);
        model.setUid(uid);
        model.setTemplate_code_list(templates);
        model.setParams(templateParams);
        model.setType(types);
        model.setRequest_id(uid + "_" + Instant.now().toEpochMilli());
        model.setUser_params(Collections.singletonMap("account", "email"));
        publisher.publish(userTouchTopic, model);
    }

    /**
     * //template_code_list对应消息类型:0:email、 1:sms 、2:极光 、3:站内信 、4:toast
     * @param userParamMap
     * @param templates
     * @param types
     * @param language
     */
    @Override
    public void kafkaNotifyMulti(Map<String, Map<String, Object>> userParamMap, List<String> templates, List<Integer> types, String language) {
        if (CollectionUtil.isEmpty(userParamMap)) return;
        long now = Instant.now().toEpochMilli();
        userParamMap.forEach((uid, templateParams) -> {
            UserTouchModel model = new UserTouchModel();
            model.setLanguage(language);
            model.setUid(uid);
            model.setParams(templateParams);
            model.setTemplate_code_list(templates);
            model.setType(types);
            model.setRequest_id(uid + "_" + now);
            model.setUser_params(Collections.singletonMap("account", "email"));
            publisher.publish(userTouchTopic, model);
        });
    }

    private Map<String, User> userMap(String uids, String phones, String emails) {
        Map<String,Object> param = new HashMap<>();
        Optional.ofNullable(uids).filter(StringUtils::isNoneBlank).ifPresent(obj -> param.put("uids",obj));
        Optional.ofNullable(phones).filter(StringUtils::isNoneBlank).ifPresent(obj -> param.put("phones",obj));
        Optional.ofNullable(emails).filter(StringUtils::isNoneBlank).ifPresent(obj -> param.put("emails",obj));
        JSONArray data = caller.get4JSONArray(domain + userDetail, Maps.of("origin_channel", "APP"), param);
        return data.stream().map(JSONObject.class::cast).map(userInfo -> userInfo.toJavaObject(User.class)).collect(Collectors.toMap(User::getUid, user -> user,(v1, v2)->v1));
    }
}
