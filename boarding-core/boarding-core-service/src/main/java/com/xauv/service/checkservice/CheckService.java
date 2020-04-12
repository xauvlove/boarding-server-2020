package com.xauv.service.checkservice;

import com.xauv.format.InternalStandardMessageFormat;

public interface CheckService {
    InternalStandardMessageFormat checkArgs(Object target, ArgsConditions conditions);
}
