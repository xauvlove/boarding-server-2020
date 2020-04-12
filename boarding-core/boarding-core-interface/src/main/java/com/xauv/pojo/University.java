package com.xauv.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "bd_university")
public class University {
    //主键
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    //高校名
    private String name;
    //高校 id -- 研招网
    private String schoolId;
    //高校唯一 id
    private Integer uniqueId;
    //创建时间
    private Date created;
    //修改时间
    private Date updated;
}
