package com.xauv.pojo.datastructure;

import lombok.Data;

import java.util.List;

@Data
public class SecondSubjectDetail {
    private String mc;
    private String dm;

    private List<ThirdSubjectWithRecruitmentNum> thirdSubWithRecruNumsList;
}