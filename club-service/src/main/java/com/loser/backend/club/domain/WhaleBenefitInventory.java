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
@Table(name = "loser_benefit_inventory")
public class loserBenefitInventory implements Serializable, MultiLocaliable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    /**
     * 所属权益id
     */
    @Column(name = "benefit_id")
    private Long benefitId;

    @Column(name = "benefit_title")
    private JSONObject benefitTitle;

    @Column(name = "benefit_descr")
    private JSONObject benefitDescr;

    @Column(name = "level_descr")
    private JSONObject levelDescr;

    @Column(name = "priority")
    private Integer priority;

    /**
     * ENABLE、DISABLE、DELETED
     */
    @Column(name = "status")
    private String status;

    @Column(name = "loser_level")
    private String loserLevel;

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
     * 权益图片
     */
    @Column(name = "benefit_img")
    private JSONObject benefitImg;

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
        sb.append(", benefitId=").append(benefitId);
        sb.append(", benefitTitle=").append(benefitTitle);
        sb.append(", benefitDescr=").append(benefitDescr);
        sb.append(", levelDescr=").append(levelDescr);
        sb.append(", priority=").append(priority);
        sb.append(", status=").append(status);
        sb.append(", loserLevel=").append(loserLevel);
        sb.append(", ctime=").append(ctime);
        sb.append(", createBy=").append(createBy);
        sb.append(", utime=").append(utime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", remark=").append(remark);
        sb.append(", benefitImg=").append(benefitImg);
        sb.append(", benefitIndicator=").append(benefitIndicator);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}