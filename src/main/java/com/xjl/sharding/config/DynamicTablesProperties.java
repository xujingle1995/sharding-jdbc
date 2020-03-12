package com.xjl.sharding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description: 
 * @author: 许京乐
 * @date:   2020/3/1 21:50
 */
@ConfigurationProperties(prefix = "dynamic.table")
@Data
public class DynamicTablesProperties {
    String[] names;
}
