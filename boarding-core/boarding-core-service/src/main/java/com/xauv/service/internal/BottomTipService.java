package com.xauv.service.internal;

import com.xauv.mapper.BottomTipMapper;
import com.xauv.pojo.BottomTip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service("internal-bottomTipService")
public class BottomTipService {

    @Autowired
    private BottomTipMapper bottomTipMapper;

    @Async
    public void saveBottomTips(List<BottomTip> bottomTipList) {
        for (BottomTip bottomTip : bottomTipList) {
            bottomTip.setId(null);
            bottomTip.setCreated(new Date());
            bottomTip.setUpdated(new Date());
        }
        bottomTipMapper.insertList(bottomTipList);
    }
}
