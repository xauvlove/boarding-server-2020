package com.xauv.pojo.wx;

import lombok.Data;

import java.io.Serializable;

@Data
public class WXLoginCallback implements Serializable {
    // session
    private String session_key;
    // open id
    private String openid;
    private Integer accessFrequency;
}
