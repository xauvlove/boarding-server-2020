package com.xauv.utils;

import com.xauv.format.InternalStandardMessageFormat;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class ConditionCheckUtil {
    public InternalStandardMessageFormat checkRequestParam(
            Object object, int conditionLength) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(object);

            if(value == null) {
                continue;
            }
            if(StringUtils.equals("null", value.toString())) {
                field.set(object, null);
                continue;
            }
            if(StringUtils.equals("", value.toString())) {
                field.set(object, null);
                continue;
            }
            conditionLength -= 1;
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
