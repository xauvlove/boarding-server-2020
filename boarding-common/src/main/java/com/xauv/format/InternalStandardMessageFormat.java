package com.xauv.format;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalStandardMessageFormat {
    private Object source;
    private Integer code;
    private String message;
    private InternalStandardMessageEnum internalStandardMessageEnum;

    public InternalStandardMessageFormat(InternalStandardMessageEnum
                                                 internalStandardMessageEnum, Object source) {
        this.internalStandardMessageEnum = internalStandardMessageEnum;
        this.source = source;
        this.code = internalStandardMessageEnum.getCode();
        this.message = internalStandardMessageEnum.getMessage();
    }

    public InternalStandardMessageFormat(InternalStandardMessageEnum internalStandardMessageEnum) {
        this.internalStandardMessageEnum = internalStandardMessageEnum;
        this.code = internalStandardMessageEnum.getCode();
        this.message = internalStandardMessageEnum.getMessage();
    }
}
