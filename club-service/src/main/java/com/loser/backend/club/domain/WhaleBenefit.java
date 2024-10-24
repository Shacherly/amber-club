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
@Table(name = "loser_benefit")
public class loserBenefit implements Serializable, MultiLocaliable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    /**
     * 权益名称
     */
    @Column(name = "benefit_name")
    private JSONObject benefitName;

    /**
     * WEALTH_MANAGEMENT、CONCIERGE_SERVICE、EXTRA_YIELD、COMMUNITY
     */
    @Column(name = "benefit_indicator")
    private String benefitIndicator;

    /**
     * 优先级1~N递减
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * ENABLE、DISABLE、DELETED
     */
    @Column(name = "status")
    private String status;

    @Column(name = "descr")
    private String descr;

    @Column(name = "ctime")
    private LocalDateTime ctime;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "utime")
    private LocalDateTime utime;

    @Column(name = "update_by")
    private String updateBy;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", benefitName=").append(benefitName);
        sb.append(", benefitIndicator=").append(benefitIndicator);
        sb.append(", priority=").append(priority);
        sb.append(", status=").append(status);
        sb.append(", descr=").append(descr);
        sb.append(", ctime=").append(ctime);
        sb.append(", createBy=").append(createBy);
        sb.append(", utime=").append(utime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}