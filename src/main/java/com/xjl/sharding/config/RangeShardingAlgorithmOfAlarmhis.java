package com.xjl.sharding.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title: dongfangdianqi
 * @description: 根据发生时间的范围查询分片算法 between and
 * @create: 2020-02-25 16:55
 * @update: 2020-02-25 16:55
 * @updateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
public class RangeShardingAlgorithmOfAlarmhis implements RangeShardingAlgorithm<Date> {

    private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMM");

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {
        Collection<String> result = new LinkedHashSet<>();

        Range<Date> shardingKey = shardingValue.getValueRange();

        // 获取起始，终止时间范围
        Date startTime = shardingKey.lowerEndpoint();
        Date endTime = shardingKey.upperEndpoint();
        Date now = new Date();
        if (startTime.after(now)){
            startTime = now;
        }
        if (endTime.after(now)){
            endTime = now;
        }
        Collection<String> tables = getRoutTable(shardingValue.getLogicTableName(), startTime, endTime);

        if (tables != null && tables.size() >0) {
            result.addAll(tables);
        }
        return result;
    }

    private Collection<String> getRoutTable(String logicTableName, Date startTime, Date endTime) {
        Set<String> rouTables = new HashSet<>();
        if (startTime != null && endTime != null) {
            List<String> rangeNameList = getRangeNameList(startTime, endTime);
            for (String YearMonth : rangeNameList) {
                rouTables.add(logicTableName + YearMonth);
            }
        }
        return rouTables;
    }

    private static List<String> getRangeNameList(Date startTime, Date endTime) {
        List<String> result = Lists.newArrayList();
        // 定义日期实例
        Calendar dd = Calendar.getInstance();

        dd.setTime(startTime);

        while(dd.getTime().before(endTime)) {
            result.add(dateformat.format(dd.getTime()));
            // 进行当前日期按月份 + 1
            dd.add(Calendar.MONTH, 1);
        }
        return result;
    }
}
