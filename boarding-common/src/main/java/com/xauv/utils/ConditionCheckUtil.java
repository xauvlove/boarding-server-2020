package com.xauv.utils;

import com.xauv.format.InternalStandardMessageFormat;

import java.lang.reflect.Field;

public class ConditionCheckUtil {
    public InternalStandardMessageFormat checkRequestParam(
            Object object, int conditionLength) throws IllegalAccessException {
        Field[] fields = object.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if(field.get(object) != null) {
                conditionLength -= 1;
            }
        }
        InternalStandardMessageFormat ismf = new InternalStandardMessageFormat();

        if(conditionLength <= 0) {
            ismf.setCode(1000);
            ismf.setMessage("success");
        } else {
            ismf.setCode(-1000);
            ismf.setMessage("failed");
        }
        return ismf;
    }
}
