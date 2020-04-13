package com.xauv.pojo.wx;

import lombok.Data;

import java.io.Serializable;

@Data
public class WXLoginCallback implements Serializable {

    private static final long serialVersionUID = -3466647352701667342L;//todo

    //最大访问频率
    public static final String ACCESS_FREQUENCY_PREFIX = "ACCESS_FREQUENCY";

    public static final Integer MAX_ACCESS_FREQUENCY = 2001;
    public static final Integer MAX_ACCESS_FREQUENCY_LANDING_EXP = 301;
    public static final Integer MAX_ACCESS_FREQUENCY_UNIVERSITY = 701;

    // session
    private String session_key;
    // open id
    private String openid;
    private Integer accessFrequency = 0;

    //访问 考研经验频率
    private Integer accFreqWithLandingExp = 0;
    //访问 高校基本信息频率
    private Integer accFreqWithUniversity = 0;

}
