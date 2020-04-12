package com.xauv.pojo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "bd_bottom_tip")
public class BottomTip {
    private Long id;
    private String prefix;
    private String content;
    private String suffix;
    private Integer frequency;
    private Date created;
    private Date updated;

    public String combineTipToString() {

        String tip = "";
        tip = tip + (StringUtils.isNotBlank(prefix)?prefix:"")
                  + (StringUtils.isNotBlank(content)?content:"")
                  + (StringUtils.isNotBlank(suffix)?suffix:"");
        return tip;
    }
}
