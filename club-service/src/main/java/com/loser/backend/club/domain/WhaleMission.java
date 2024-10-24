package com.loser.backend.club.domain;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.loser.backend.club.domain.base.MultiLocaliable;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Accessors(chain = true)
@Table(name = "loser_mission")
public class loserMission implements Serializable, MultiLocaliable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    @Column(name = "mission_title")
    private JSONObject missionTitle;

    /**
     * 所属权益分类
     */
    @Column(name = "benefit_id")
    private Long benefitId;

    /**
     * 权属权益清单
     */
    @Column(name = "benefit_inventory_id")
    private Long benefitInventoryId;

    @Column(name = "mission_descr")
    private JSONObject missionDescr;

    @Column(name = "mission_icon")
    private JSONObject missionIcon;

    @Column(name = "mission_img")
    private JSONObject missionImg;

    /**
     * 跳转链接
     */
    @Column(name = "forward_link")
    private JSONObject forwardLink;

    @Column(name = "priority")
    private Integer priority;

    /**
     * ROUTINE、CUSTOM
     */
    @Column(name = "mission_type")
    private String missionType;

    /**
     * 有效起
     */
    @Column(name = "valid_begin")
    private LocalDateTime validBegin;

    /**
     * 有效止
     */
    @Column(name = "valid_end")
    private LocalDateTime validEnd;

    /**
     * ENABLE、DISABLE、DELETED、EXPIRED
     */
    @Column(name = "status")
    private String status;

    /**
     * 关联的客户类型ALL SELECTED_USER IMPORTED_USER USER_LABEL SINGLE_USER
     */
    @Column(name = "client_type")
    private String clientType;

    /**
     * 客户参数（标签、uid等）
     */
    @Column(name = "client_attaches")
    private String clientAttaches;

    @Column(name = "ctime")
    private LocalDateTime ctime;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "utime")
    private LocalDateTime utime;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "remark")
    private String remark;

    /**
     * WEALTH_MANAGEMENT、CONCIERGE_SERVICE、EXTRA_YIELD、COMMUNITY
     */
    @Column(name = "benefit_indicator")
    private String benefitIndicator;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", missionTitle=").append(missionTitle);
        sb.append(", benefitId=").append(benefitId);
        sb.append(", benefitInventoryId=").append(benefitInventoryId);
        sb.append(", missionDescr=").append(missionDescr);
        sb.append(", missionIcon=").append(missionIcon);
        sb.append(", missionImg=").append(missionImg);
        sb.append(", forwardLink=").append(forwardLink);
        sb.append(", priority=").append(priority);
        sb.append(", missionType=").append(missionType);
        sb.append(", validBegin=").append(validBegin);
        sb.append(", validEnd=").append(validEnd);
        sb.append(", status=").append(status);
        sb.append(", clientType=").append(clientType);
        sb.append(", clientAttaches=").append(clientAttaches);
        sb.append(", ctime=").append(ctime);
        sb.append(", createBy=").append(createBy);
        sb.append(", utime=").append(utime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", remark=").append(remark);
        sb.append(", benefitIndicator=").append(benefitIndicator);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}