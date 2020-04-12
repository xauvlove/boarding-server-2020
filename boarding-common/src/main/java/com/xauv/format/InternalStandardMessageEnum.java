package com.xauv.format;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalStandardMessageEnum {
    DAP_INSERT_RECORD_SUCCESS("增加数据成功", 1),
    DAO_INSERT_RECORD_FAILED("增加数据失败", -1),
    DAP_DELETE_RECORD_SUCCESS("删除数据成功", 2),
    DAO_DELETE_RECORD_FAILED("删除数据失败", -2),
    DAO_UPDATE_RECORD_SUCCESS("更新数据成功", 3),
    DAO_UPDATE_RECORD_FAILED("更新数据失败", -3),
    DAO_QUERY_RECORD_SUCCESS("查询数据成功", 4),
    DAO_QUERY_RECORD_FAILED("查询数据失败", -4),

    VALID_REQUEST_OBJECT("请求检查的参数类型匹配", 21),
    INVALID_REQUEST_OBJECT("请求检查的参数类型不匹配", -21)

    ;
    String message;
    Integer code;
}
