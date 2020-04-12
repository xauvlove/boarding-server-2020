package com.xauv.service.dispatcherservice;

import com.xauv.format.InternalStandardMessageFormat;

public interface DispatcherService {
    InternalStandardMessageFormat doDispatcher(Object target);
}
