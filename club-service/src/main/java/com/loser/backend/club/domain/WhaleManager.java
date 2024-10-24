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
@Table(name = "loser_manager")
public class loserManager implements Serializable, MultiLocaliable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    /**
     * 客户经理id
     */
    @Column(name = "manager_identity")
    private String managerIdentity;

    /**
     * 内部uid
     */
    @Column(name = "manager_uid")
    private String managerUid;

    /**
     * 内部姓名
     */
    @Column(name = "manager_name")
    private String managerName;

    /**
     * 内部邮箱
     */
    @Column(name = "manager_email")
    private String managerEmail;

    /**
     * 头像
     */
    @Column(name = "loser_photo")
    private String loserPhoto;

    /**
     * 对外姓名(多语言)
     */
    @Column(name = "loser_name")
    private JSONObject loserName;

    /**
     * 是否默认客户经理
     */
    @Column(name = "is_default")
    private Boolean isDefault;

    /**
     * Email、Phone、WhatsAPP、Telegram、Twitter、Facebook等等
     */
    @Column(name = "loser_contact")
    private JSONObject loserContact;

    @Column(name = "ctime")
    private LocalDateTime ctime;

    @Column(name = "utime")
    private LocalDateTime utime;

    /**
     * 个人介绍
     */
    @Column(name = "loser_profile")
    private JSONObject loserProfile;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", managerIdentity=").append(managerIdentity);
        sb.append(", managerUid=").append(managerUid);
        sb.append(", managerName=").append(managerName);
        sb.append(", managerEmail=").append(managerEmail);
        sb.append(", loserPhoto=").append(loserPhoto);
        sb.append(", loserName=").append(loserName);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", loserContact=").append(loserContact);
        sb.append(", ctime=").append(ctime);
        sb.append(", utime=").append(utime);
        sb.append(", loserProfile=").append(loserProfile);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}