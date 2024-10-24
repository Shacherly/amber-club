package com.loser.backend.club.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "loser_mission_binding")
public class loserMissionBinding implements Serializable {
    @Column(name = "uid")
    private String uid;

    @Column(name = "mission_id")
    private Long missionId;

    @Column(name = "ctime")
    private LocalDateTime ctime;

    @Column(name = "utime")
    private LocalDateTime utime;

    private static final long serialVersionUID = 1L;

    public loserMissionBinding(String uid, Long missionId, LocalDateTime ctime, LocalDateTime utime) {
        this.uid = uid;
        this.missionId = missionId;
        this.ctime = ctime;
        this.utime = utime;
    }

    public loserMissionBinding(String uid, Long missionId) {
        this.uid = uid;
        this.missionId = missionId;
    }

    public loserMissionBinding() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uid=").append(uid);
        sb.append(", missionId=").append(missionId);
        sb.append(", ctime=").append(ctime);
        sb.append(", utime=").append(utime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}