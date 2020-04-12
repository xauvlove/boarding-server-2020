package com.xauv.web;

import com.xauv.service.daoservice.BottomTipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tip")
public class BottomTipController {

    @Autowired
    private BottomTipService bottomTipService;

    @GetMapping("/bottom")
    public ResponseEntity<String> getStochasticTip() {
        String randomTip = bottomTipService.getOneRandomTip();
        if(StringUtils.isNotBlank(randomTip)) {
            return ResponseEntity.ok(randomTip);
        } else {
            return ResponseEntity.ok("");
        }
    }
}
