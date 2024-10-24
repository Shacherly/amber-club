package com.loser.backend.club.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Accessors(chain = true)
@Table(name = "loser_level_history")
public class loserLevelHistory implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "ctime")
    private LocalDateTime ctime;

    /**
     * loser_LEVEL
     */
    @Column(name = "target")
    private String target;

    /**
     * 变更前
     */
    @Column(name = "old_value")
    private String oldValue;

    /**
     * 变更后
     */
    @Column(name = "latest_value")
    private String latestValue;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "remark")
    private String remark;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", ctime=").append(ctime);
        sb.append(", target=").append(target);
        sb.append(", oldValue=").append(oldValue);
        sb.append(", latestValue=").append(latestValue);
        sb.append(", createBy=").append(createBy);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}