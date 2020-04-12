package com.xauv.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "bd_university_direction")
public class UniversityDirection {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long universityId;
    //冗余字段
    private String universityName;
    //学硕 = 1 专硕 = 2，非全 =  博士 = ?
    private Integer masterType;
    //一级学科<门类>：哲学，工学，法学
    private String broadDirectionName;
    private String broadDirectionCode;

    //二级学科<学科类别>：计算机科学与技术
    private String involvedDirectionName;
    private String involvedDirectionCode;

    //三级学科：计算机科学与技术，网络空间安全
    private String majorDirectionName;
    private String majorDirectionCode;

    //统考？
    private String examMode;
    //所在的研究所
    private String instituteName;
    //研究所代码
    private String instituteCode;

    //专业，和 majorDirectionName 是一致的
    private String subjectName;
    private String subjectCode;

    //<专业>研究方向：不区分研究方向，或区分，比如机器学习，数据挖掘等
    private String directionName;
    private String directionCode;

    //学习方式
    private String learningMode;

    //<专业>研究方向 的招生人数
    private String recruitmentCount;

    //考试范围的网站
    private String examRangeContent;

    private Date created;
    private Date updated;

}
