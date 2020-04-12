package com.xauv.service.checkservice;

import com.xauv.format.InternalStandardMessageEnum;
import com.xauv.format.InternalStandardMessageFormat;
import com.xauv.pojo.vo.LandingExperienceShareVO;
import com.xauv.service.daoservice.LandingExperienceDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;

@Service
public class LandingExperienceVOCheckService implements CheckService{

    @Autowired
    private LandingExperienceDaoService landingExperienceDaoService;

    @Override
    public InternalStandardMessageFormat checkArgs(Object target, ArgsConditions conditions) {
        if(!(target instanceof LandingExperienceShareVO)) {
            return new InternalStandardMessageFormat(InternalStandardMessageEnum.INVALID_REQUEST_OBJECT);
        }
        //参数检查
        LandingExperienceShareVO vo = (LandingExperienceShareVO) target;
        Field[] fields = vo.getClass().getDeclaredFields();
        int notNullCount = conditions.getNotNullCount();
        for (Field field : fields) {
            try {
                if(field.get(vo) != null) {
                    notNullCount --;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(notNullCount > 0) {
            return new InternalStandardMessageFormat(InternalStandardMessageEnum.INVALID_REQUEST_OBJECT);
        }
        return landingExperienceDaoService.saveLandingExperience(vo);
    }
}
