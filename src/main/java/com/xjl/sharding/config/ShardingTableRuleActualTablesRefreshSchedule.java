package com.xjl.sharding.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.core.exception.ShardingConfigurationException;
import org.apache.shardingsphere.core.rule.DataNode;
import org.apache.shardingsphere.core.rule.TableRule;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description:水平分表，动态分表刷新定时任务
 * @author: 许京乐
 * @date: 2020/2/29 23:47
 */
@Component
@EnableScheduling
@EnableConfigurationProperties(DynamicTablesProperties.class)
@Slf4j
public class ShardingTableRuleActualTablesRefreshSchedule implements InitializingBean {

    @Autowired
    private DynamicTablesProperties dynamicTables;

    @Autowired
    private DataSource dataSource;

    public ShardingTableRuleActualTablesRefreshSchedule() {
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void actualTablesRefresh() throws NoSuchFieldException, IllegalAccessException {
        System.out.println("---------------------------------");
        ShardingDataSource dataSource = (ShardingDataSource) this.dataSource;
        if (dynamicTables.getNames() == null || dynamicTables.getNames().length == 0) {
            log.warn("dynamic.table.names为空");
            return;
        }
        for (int i = 0; i < dynamicTables.getNames().length; i++) {
            TableRule tableRule = null;
            try {
                tableRule = dataSource.getShardingContext().getShardingRule().getTableRule(dynamicTables.getNames()[i]);
                System.out.println(tableRule);
            } catch (ShardingConfigurationException e) {
                log.error("逻辑表：{},不存在配置！", dynamicTables.getNames()[i]);
            }
            List<DataNode> dataNodes = tableRule.getActualDataNodes();

            Field actualDataNodesField = TableRule.class.getDeclaredField("actualDataNodes");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(actualDataNodesField, actualDataNodesField.getModifiers() & ~Modifier.FINAL);
            actualDataNodesField.setAccessible(true);

            LocalDateTime localDateTime = LocalDateTime.of(2019, 12, 1, 0, 0, new Random().nextInt(59));
            LocalDateTime now = LocalDateTime.now();

            String dataSourceName = dataNodes.get(0).getDataSourceName();
            String logicTableName = tableRule.getLogicTable();
            StringBuilder stringBuilder = new StringBuilder(10).append(dataSourceName).append(".").append(logicTableName);
            final int length = stringBuilder.length();
            List<DataNode> newDataNodes = new ArrayList<>();
            while (true) {
                stringBuilder.setLength(length);
                stringBuilder.append(localDateTime.format(DateTimeFormatter.ofPattern("yyyyMM")));
                DataNode dataNode = new DataNode(stringBuilder.toString());
                newDataNodes.add(dataNode);
                localDateTime = localDateTime.plusMonths(1L);
                if (localDateTime.isAfter(now)) {
                    break;
                }
            }
            actualDataNodesField.set(tableRule, newDataNodes);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        actualTablesRefresh();
    }
}
