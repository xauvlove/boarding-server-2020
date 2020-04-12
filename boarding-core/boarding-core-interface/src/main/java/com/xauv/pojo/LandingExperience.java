package com.xauv.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "bd_landing_experience")
public class LandingExperience {
    //自身id
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    //高校id
    private Long universityId;
    //高校名
    private String universityName;
    //门类
    private String broadDirectionName;
    //一级学科
    private String involvedDirectionName;
    //二级学科
    private String majorDirectionName;
    //考研届
    private String examDate;
    //考研初试总分
    private String examMark;
    //初试名次
    private String examRank;
    //复试分数线
    private String reExamMarkLimit;
    //复试总分（可能是加权的）
    private String reExamMark;
    //联系方式
    private String contactInfo;
    //硕士类型
    private Integer masterType;
    //考研经验分享
    private String experience;

    private Date created;
    private Date updated;
}
