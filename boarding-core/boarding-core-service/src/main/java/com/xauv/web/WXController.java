package com.xauv.web;

import com.xauv.exception.AESEncodeException;
import com.xauv.service.WXLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/wx")
public class WXController {

    @Autowired
    private WXLoginService wxLoginService;

    @PostMapping("/login")
    public Map<String, Object> wxLogin(@RequestParam("code") String code) throws AESEncodeException {
        return wxLoginService.Login(code);
    }
}
