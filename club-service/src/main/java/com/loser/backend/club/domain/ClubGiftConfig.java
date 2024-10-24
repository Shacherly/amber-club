package com.loser.backend.club.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Accessors(chain = true)
@Table(name = "club_gift_config")
public class ClubGiftConfig implements Serializable {
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
     * 对应的礼物id(券id、理财产品id)
     */
    @Column(name = "gift_id")
    private String giftId;

    /**
     * 礼物类型（前缀earn_0000,coupon_0000）
     */
    @Column(name = "gift_type")
    private String giftType;

    /**
     * 优先级
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 基准利率
     */
    @Column(name = "standard_rate")
    private BigDecimal standardRate;

    /**
     * 专属利率
     */
    @Column(name = "exclusive_rate")
    private BigDecimal exclusiveRate;

    /**
     * 优先级更新时间
     */
    @Column(name = "sort_update_time")
    private LocalDateTime sortUpdateTime;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "is_effect")
    private Boolean isEffect;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", clubLevel=").append(clubLevel);
        sb.append(", giftId=").append(giftId);
        sb.append(", giftType=").append(giftType);
        sb.append(", sort=").append(sort);
        sb.append(", standardRate=").append(standardRate);
        sb.append(", exclusiveRate=").append(exclusiveRate);
        sb.append(", sortUpdateTime=").append(sortUpdateTime);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", isEffect=").append(isEffect);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}