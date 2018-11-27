package com.cecgw.cq.controller;

import com.cecgw.cq.service.RedisFlushService;
import com.cecgw.cq.util.RedisFlushResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther:lifuyi
 * @Date: 2018/11/26 09:41
 * @Description:
 */
@Controller
@RequestMapping(value = "/redisFlush")
public class RedisFlushController {

    @Autowired
    private RedisFlushService redisFlushService;

    @RequestMapping(value = "/flush", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RedisFlushResult RedisFlushOpt(@RequestParam String tableName){
        List<String> tableList=new ArrayList<>();
        tableList.add(tableName);
        return redisFlushService.flushRedis(tableList);
    }

}
