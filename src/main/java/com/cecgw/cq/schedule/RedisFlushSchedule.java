package com.cecgw.cq.schedule;

import com.cecgw.cq.service.RedisFlushService;
import com.cecgw.cq.util.JedisUtil;
import com.cecgw.cq.util.RedisFlushResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther:lifuyi
 * @Date: 2018/11/22 16:36
 * @Description:每天刷新redis
 */
@Component
public class RedisFlushSchedule {

    @Autowired
    private   RedisFlushService redisFlushService;

    @Scheduled(cron = "59 59 23 * * ?")
    public void flushRedis(){
        String tableName1="t_epc_dict_1";
        String tableName2="t_epc_dict_1_copy1";
        List<String> tableNameList=new ArrayList<>();
        tableNameList.add(tableName1);
        tableNameList.add(tableName2);
        RedisFlushResult redisFlushResult=redisFlushService.flushRedis(tableNameList);
    }

}
