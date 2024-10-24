package com.loser.backend.club.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Accessors(chain = true)
@Table(name = "loser_client_relation")
public class loserClientRelation implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    @Column(name = "uid")
    private String uid;

    /**
     * manager identity
     */
    @Column(name = "mid")
    private String mid;

    @Column(name = "ctime")
    private LocalDateTime ctime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", mid=").append(mid);
        sb.append(", ctime=").append(ctime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}