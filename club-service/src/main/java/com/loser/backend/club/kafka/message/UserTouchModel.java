package com.loser.backend.club.kafka.message;


import com.loser.backend.club.kafka.publisher.AbstractProducerModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor @Getter @Setter
public class UserTouchModel extends AbstractProducerModel implements Serializable {
    private static final long serialVersionUID = 7749543619361663972L;

    //template_code_list对应消息类型:0:email、 1:sms 、2:极光 、3:站内信 、4:toast
    public static final Integer TYPE_EMAIL=0;
    public static final Integer TYPE_SMS=1;
    public static final Integer TYPE_AURORA=2;
    public static final Integer TYPE_STATION_LETTER=3;
    public static final Integer TYPE_STATION_TOAST=4;

    private String language;

    private Map<String, Object> params;

    private String request_id;

    private String system_id = "club";

    private List<String> template_code_list;

    private List<Integer> type;

    private String uid;

    private Map<String, Object> user_params;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserTouchModel{");
        sb.append("language='").append(language).append('\'');
        sb.append(", params=").append(params);
        sb.append(", request_id='").append(request_id).append('\'');
        sb.append(", system_id='").append(system_id).append('\'');
        sb.append(", template_code_list=").append(template_code_list);
        sb.append(", type=").append(type);
        sb.append(", uid='").append(uid).append('\'');
        sb.append(", user_params=").append(user_params);
        sb.append('}');
        return sb.toString();
    }
}
