package com.xauv.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UniversityDirectionVOContent {

    //学硕 = 1 专硕 = 2，非全 =  博士 = ?
    private Integer masterType;

    private String obscureId;
    private String obscureUniversityId;
    //冗余字段
    private String universityName;

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
    private String direction;

    //学习方式
    private String learningMode;

    //<专业>研究方向 的招生人数
    private String recruitmentCount;

    //考试范围的网站
    private String examRangeContent;

    //最新时间，指最新拉取的数据时间
    private Date newestPull;
}
