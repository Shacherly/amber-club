package com.loser.backend.club.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Accessors(chain = true)
@Table(name = "club_level")
public class ClubLevel implements Serializable, ClubUpgrade {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    /**
     * 用户唯一id
     */
    @Column(name = "uid")
    private String uid;

    /**
     * club等级
     */
    @Column(name = "club_level")
    private Integer clubLevel;

    /**
     * club值
     */
    @Column(name = "club_point")
    private Long clubPoint;

    /**
     * 有效期（下个自然月1号）
     */
    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    /**
     * club等级更新时间戳
     */
    @Column(name = "upgrade_time")
    private LocalDateTime upgradeTime;

    /**
     * 升级已读
     */
    @Column(name = "upgrade_read")
    private Boolean upgradeRead;

    @Column(name = "ctime")
    private LocalDateTime ctime;

    @Column(name = "utime")
    private LocalDateTime utime;

    @Column(name = "remark")
    private String remark;

    /**
     * bule loser等级
     */
    @Column(name = "loser_level")
    private Integer loserLevel;

    /**
     * 权益等级
     */
    @Column(name = "benefit_level")
    private Integer benefitLevel;

    /**
     * bule loser权益有效期
     */
    @Column(name = "loser_until")
    private LocalDateTime loserUntil;

    /**
     * blue loser 首次访问？
     */
    @Column(name = "first_access")
    private Boolean firstAccess;

    @Column(name = "loser_upgrade_time")
    private LocalDateTime loserUpgradeTime;

    @Column(name = "loser_upgrade_read")
    private Boolean loserUpgradeRead;

    /**
     * 首页首次访问？
     */
    @Column(name = "home_first_access")
    private Boolean homeFirstAccess;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", clubLevel=").append(clubLevel);
        sb.append(", clubPoint=").append(clubPoint);
        sb.append(", validUntil=").append(validUntil);
        sb.append(", upgradeTime=").append(upgradeTime);
        sb.append(", upgradeRead=").append(upgradeRead);
        sb.append(", ctime=").append(ctime);
        sb.append(", utime=").append(utime);
        sb.append(", remark=").append(remark);
        sb.append(", loserLevel=").append(loserLevel);
        sb.append(", benefitLevel=").append(benefitLevel);
        sb.append(", loserUntil=").append(loserUntil);
        sb.append(", firstAccess=").append(firstAccess);
        sb.append(", loserUpgradeTime=").append(loserUpgradeTime);
        sb.append(", loserUpgradeRead=").append(loserUpgradeRead);
        sb.append(", homeFirstAccess=").append(homeFirstAccess);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}