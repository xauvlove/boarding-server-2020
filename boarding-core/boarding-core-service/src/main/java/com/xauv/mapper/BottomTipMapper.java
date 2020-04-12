package com.xauv.mapper;

import com.xauv.pojo.BottomTip;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface BottomTipMapper extends Mapper<BottomTip> {
    @Select("select count(*) from bd_bottom_tip")
    int getRecordCount();

    @Select("select * from bd_bottom_tip limit 1")
    BottomTip getFirstRecordFromDB();

    @Select("select * from bd_bottom_tip where id = #{id}")
    BottomTip selectRecordById(@Param("id") Long id);

    @Update("update bd_bottom_tip set frequency=#{frequency} where id=#{id}")
    int increaseUseFrequencyById(@Param("id") Long id, @Param("frequency") Integer frequency);
}
