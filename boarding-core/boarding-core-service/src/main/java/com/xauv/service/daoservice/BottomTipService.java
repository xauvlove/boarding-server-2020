package com.xauv.service.daoservice;

import com.xauv.mapper.BottomTipMapper;
import com.xauv.pojo.BottomTip;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BottomTipService {

    @Autowired
    private BottomTipMapper bottomTipMapper;

    public String getOneRandomTip() {
        BottomTip firstRecord = bottomTipMapper.getFirstRecordFromDB();
        //第一条记录 id
        long min = firstRecord.getId();
        //总共记录数
        int recordCount = bottomTipMapper.getRecordCount();
        long candidateId = RandomUtils.nextLong(min, recordCount + 1);
        BottomTip bottomTip = bottomTipMapper.selectRecordById(candidateId);
        updateUseFrequency(bottomTip);
        return bottomTip.combineTipToString();
    }

    @Async
    public void updateUseFrequency(BottomTip bottomTip) {
        if(bottomTip.getFrequency() == null) {
            bottomTip.setFrequency(1);
        } else {
            bottomTip.setFrequency(bottomTip.getFrequency() + 1);
        }
        System.out.println(bottomTip.getFrequency());
        bottomTipMapper.increaseUseFrequencyById(bottomTip.getId(), bottomTip.getFrequency());
    }
}
