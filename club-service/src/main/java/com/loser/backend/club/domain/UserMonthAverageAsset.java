package com.loser.backend.club.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "user_month_average_asset")
public class UserMonthAverageAsset implements Serializable {
    @Id
    @Column(name = "uid")
    private String uid;

    /**
     * 近30日均资产
     */
    @Column(name = "ave_asset")
    private BigDecimal aveAsset;

    /**
     * 更新时间
     */
    @Column(name = "utime")
    private LocalDateTime utime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uid=").append(uid);
        sb.append(", aveAsset=").append(aveAsset);
        sb.append(", utime=").append(utime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}