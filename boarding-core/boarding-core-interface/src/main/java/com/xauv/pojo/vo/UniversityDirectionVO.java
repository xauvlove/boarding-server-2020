package com.xauv.pojo.vo;

import lombok.Data;

@Data
public class UniversityDirectionVO {

    /**
     * 给前端设定的属性
     */
    private String fid;
    private Boolean open = false;

    private UniversityDirectionVOContent universityDirectionVOContent;
}
