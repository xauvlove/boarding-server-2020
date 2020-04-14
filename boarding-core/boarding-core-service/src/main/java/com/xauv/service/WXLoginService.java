package com.xauv.service;

import com.xauv.config.properties.AppConfigurationProperties;
import com.xauv.exception.AESEncodeException;
import com.xauv.utils.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@EnableConfigurationProperties(AppConfigurationProperties.class)
public class WXLoginService {

    private static final String WX_LOGIN_USER_INFO_REDIS_PREFIX = "WX_LOGIN";

    @Autowired
    private AppConfigurationProperties appProperties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Map<String, Object> Login(String code) throws AESEncodeException {
        Map<String, Object> result = new HashMap<>();

        String url = appProperties.getBaseUrl() + appProperties.getAppId() + "&secret="
                + appProperties.getSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        //微信响应一个json
        String json = restTemplate.getForObject(url, String.class);

        if(StringUtils.contains(json, "errcode")) {
            result.put("status", 500);
            return result;
        }
        //保存用户登录状态到 redis  --  存储日期 = 7天
        assert json != null;
        redisTemplate.opsForValue().set(WX_LOGIN_USER_INFO_REDIS_PREFIX, json, 7L, TimeUnit.DAYS);
        result.put("status", 200);
        //存储到小程序客户端可能有点长 这里可能存在问题
        //如果有这个问题，那么就要更换加密算法
        result.put("ticket", AESUtil.encryptEcbMode(json));
        return result;
    }
}
