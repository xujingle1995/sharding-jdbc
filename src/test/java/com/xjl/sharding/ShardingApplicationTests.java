package com.xjl.sharding;

import com.xjl.sharding.dao.TestDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingApplicationTests {

    @Autowired
    TestDao testDao;
    @Test
    public void contextLoads() {
        testDao.getAlarmHistoryById("1");
    }

}
