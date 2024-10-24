package com.loser.backend.club.client;

import com.loser.backend.club.pojo.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IUserServiceApi {

    Map<String, User> userMap(Collection<String> uids, Collection<String> phones, Collection<String> emails);

    User userInfo(String uid);

    void kafkaNotify(String uid, List<String> templates, Map<String, Object> templateParams, List<Integer> types, String language);

    /**
     * //template_code_list对应消息类型:0:email、 1:sms 、2:极光 、3:站内信 、4:toast
     * @param userParamMap
     * @param templates
     * @param types
     * @param language
     */
    void kafkaNotifyMulti(Map<String, Map<String, Object>> userParamMap, List<String> templates, List<Integer> types, String language);
}
