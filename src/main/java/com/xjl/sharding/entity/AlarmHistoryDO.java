package com.xjl.sharding.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;

/**
 * @Description:
 * @author: 许京乐
 * @date: 2020/2/29 16:11
 */
@Data
public class AlarmHistoryDO {
    private int turbineID;

    private int almPointID;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant almHappenTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant almClearTime;
}
