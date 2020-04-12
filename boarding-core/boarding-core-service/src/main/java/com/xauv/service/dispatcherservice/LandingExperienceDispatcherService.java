package com.xauv.service.dispatcherservice;

import com.xauv.format.InternalStandardMessageFormat;
import com.xauv.service.checkservice.ArgsConditions;
import com.xauv.service.checkservice.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LandingExperienceDispatcherService implements DispatcherService{

    @Autowired
    private CheckService LandingExCheckService;

    @Override
    public InternalStandardMessageFormat doDispatcher(Object target) {
        ArgsConditions conditions = new ArgsConditions();
        conditions.setNotNullCount(0);
        return LandingExCheckService.checkArgs(target, conditions);
    }
}
