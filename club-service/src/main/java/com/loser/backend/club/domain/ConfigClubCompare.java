package com.loser.backend.club.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Accessors(chain = true)
@Table(name = "config_club_compare")
public class ConfigClubCompare implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    /**
     * 当前club等级
     */
    @Column(name = "club_level")
    private Integer clubLevel;

    /**
     * 对比club等级
     */
    @Column(name = "club_level_cp")
    private Integer clubLevelCp;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    /**
     * 礼物类型（前缀earn_0000,coupon_0000）
     */
    @Column(name = "gift_type")
    private String giftType;

    /**
     * 是否生效（1-是 0-否）
     */
    @Column(name = "is_effect")
    private Integer isEffect;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", clubLevel=").append(clubLevel);
        sb.append(", clubLevelCp=").append(clubLevelCp);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", giftType=").append(giftType);
        sb.append(", isEffect=").append(isEffect);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}