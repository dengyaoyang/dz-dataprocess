package com.cecgw.cq.service;

import com.cecgw.cq.util.RedisFlushResult;

import java.util.List;

/**
 * @Auther:lifuyi
 * @Date: 2018/11/23 10:41
 * @Description:
 */
public interface RedisFlushService {
    RedisFlushResult flushRedis(List<String> tableName);
}
