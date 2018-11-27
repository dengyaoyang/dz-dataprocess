package com.cecgw.cq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cecgw.cq.service.RedisFlushService;
import com.cecgw.cq.util.JedisUtil;
import com.cecgw.cq.util.RedisFlushResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther:lifuyi
 * @Date: 2018/11/23 10:42
 * @Description:刷新Redis配置表操作
 */
@Service
public class RedisFlushServiceImpl implements RedisFlushService{

    @Autowired
    JedisUtil jedisUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public RedisFlushResult flushRedis(List<String> tableNameList) {
        RedisFlushResult redisFlushResult=new RedisFlushResult();
        //判断redis中有没有这个key
        for(String string:tableNameList){
            if(!jedisUtil.exists(string)){
                redisFlushResult.setCode(1);
                redisFlushResult.setMessage("表"+string+"在redis中不存在");
                return redisFlushResult;
            }
        }
        //查询ES和redis的数据并比对，对于不一样的表进行重新缓存
        for(String string:tableNameList){
            //先判断字段是否相同
            //mysql字段
            List<String> oracleColumnNames = jdbcTemplate.queryForList("select COLUMN_NAME from " +
                    "information_schema.COLUMNS where table_name = '"+string+"'", String.class);
            //redis字段
            List<String> redisString = jedisUtil.getList(string);
            JSONObject oracleObject = JSON.parseObject(redisString.get(0));
            Set<String> redisColumnNames = oracleObject.getInnerMap().keySet();
            for(String str:oracleColumnNames){
                if(!redisColumnNames.contains(str)){
                    redisFlushResult.setCode(2);
                    redisFlushResult.setMessage("表"+string+"在redis和oracle中的字段不匹配");
                    return redisFlushResult;
                }
            }

            //判断数据是否相同
            Map<String, Object> map = jdbcTemplate.queryForMap("select count(1) num from "+string);
            Long num = (Long) map.get("num");
            boolean flag=true;
            //oracle中的
            List<Map<String, Object>> oracleValueMaps=jdbcTemplate.queryForList("select * from " + string);
            if(num==redisString.size()){
                //数量相等，再查询具体数据是否一样
                //redis中的
                List<Map<String, Object>> redisValueMaps=new ArrayList<>();
                for(String redisStr:redisString){
                    JSONObject jsonObject = JSON.parseObject(redisStr);
                    Map<String, Object> redisValue = jsonObject.getInnerMap();
                    redisValueMaps.add(redisValue);
                }

                Map<String, Object> oracleMapFrist;
                Map<String, Object> redisMap;
                for(int i=0;i<oracleValueMaps.size();i++){
                    oracleMapFrist = oracleValueMaps.get(i);
                    Map<String, String> oracleMap=new HashMap<>();
                    for(Map.Entry<String, Object> entry:oracleMapFrist.entrySet()){
                        oracleMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                    redisMap= redisValueMaps.get(i);
                    if(!oracleMap.equals(redisMap)){
                        flag=false;
                        break;
                    }
                }
            }else {
                flag=false;
            }
            //更新
            if(!flag){
                List<String> addList=new ArrayList<>();
                for(Map<String, Object> oracleMap:oracleValueMaps){
                    String jsonStr = JSON.toJSONString(oracleMap);
                    addList.add(jsonStr);
                }
                jedisUtil.setList(string,addList,0);
                redisFlushResult.setCode(3);
                redisFlushResult.setMessage("修改成功");
            }else {
                redisFlushResult.setCode(4);
                redisFlushResult.setMessage("无需修改");
            }
        }
        return redisFlushResult;
    }

}
