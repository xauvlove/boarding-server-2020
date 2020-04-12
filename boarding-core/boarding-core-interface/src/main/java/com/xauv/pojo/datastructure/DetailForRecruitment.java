package com.xauv.pojo.datastructure;

import lombok.Data;

import java.util.List;

@Data
public class DetailForRecruitment {

    private String schoolName;
    private String schoolId;
    private List<FirstSubjectDetail> firstSubjectDetailList;

}
