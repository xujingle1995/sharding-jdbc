package com.xjl.sharding.dao;

import com.xjl.sharding.entity.AlarmHistoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

/**
 * @Description:历史报警信息表数据类
 * @author: 许京乐
 * @date: 2020/2/29 16:06
 */
@Mapper
public interface TestDao {

    List<AlarmHistoryDO> getAlarmHistoryById(@Param("id") String id);

    List<AlarmHistoryDO> getAlarmHistoryByTime(@Param("startTime") Instant startTime, @Param("endTime") Instant endTime);
}
