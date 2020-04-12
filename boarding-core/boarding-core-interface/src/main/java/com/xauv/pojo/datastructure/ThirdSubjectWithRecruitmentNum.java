package com.xauv.pojo.datastructure;

import lombok.Data;

@Data
public class ThirdSubjectWithRecruitmentNum {
    //考试方式
    private String testMode;
    //院系所
    private String institute;
    //专业
    private String subject;
    //研究方向
    private String direction;
    //学习方式
    private String learningMode;
    //招生人数
    private String number;
    //考试范围
    private String testRangeSite;
}
