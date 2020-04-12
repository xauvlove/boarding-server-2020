package com.xauv.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "bd_university_detail")
public class UniversityDetail {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    //关联 university 表的 id
    private Long universityId;
    //是否有研究生院
    private Boolean hasPostInstitute;
    //是否为自划线高校
    private Boolean selfRegularMarkLine;
    //隶属机构
    private String belongsTo;
    //分类 id，不知道是啥玩意 有这个才能访问研招网的高校信息
    //private Long categoryId;
    //高校地址，归属地
    private String location;
    //学校的别名
    private String alias;
    //咨询网站
    private String questionSite;
    //招生简章
    private String recruitmentSite;
    private String nirvanaSite;
    private Date created;
    private Date updated;
}
