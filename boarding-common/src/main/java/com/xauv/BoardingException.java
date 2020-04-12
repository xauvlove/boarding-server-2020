package com.xauv;

import com.xauv.format.InternalStandardMessageFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardingException extends Exception{
    private InternalStandardMessageFormat internalStandardMessageFormat;
}
