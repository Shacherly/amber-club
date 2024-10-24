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
@Table(name = "loser_report")
public class loserReport implements Serializable, MultiLocaliable {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false)
    private Long id;

    @Column(name = "report_title")
    private JSONObject reportTitle;

    /**
     * OBSERVE、RESEARCH
     */
    @Column(name = "report_class")
    private String reportClass;

    @Column(name = "report_screenshot")
    private JSONObject reportScreenshot;

    @Column(name = "report_content")
    private JSONObject reportContent;

    /**
     * 研报文件
     */
    @Column(name = "report_source")
    private JSONObject reportSource;

    /**
     * ENABLE、DISABLE
     */
    @Column(name = "status")
    private String status;

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

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", reportTitle=").append(reportTitle);
        sb.append(", reportClass=").append(reportClass);
        sb.append(", reportScreenshot=").append(reportScreenshot);
        sb.append(", reportContent=").append(reportContent);
        sb.append(", reportSource=").append(reportSource);
        sb.append(", status=").append(status);
        sb.append(", ctime=").append(ctime);
        sb.append(", createBy=").append(createBy);
        sb.append(", utime=").append(utime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}