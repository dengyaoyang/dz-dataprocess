package com.cecgw.cq.service.impl;

import com.cecgw.cq.service.DeleteOriginRfidService;
import com.cecgw.cq.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

/**
 * @Author zhuguanghui
 * @Description
 * @Date 2018/11/19
 */

@Service
public class DeleteOriginRfidServiceImpl implements  DeleteOriginRfidService {

    @Autowired
    JedisUtil jedisUtil;

    DateFormat df = new SimpleDateFormat("yyyyMMdd");
    Calendar calendar = Calendar.getInstance();
    String date = df.format(calendar.getTime());

    @Override
    public void deleteData(){
        Set<String> keys = jedisUtil.getResource().keys("RFID"+date+"*");
        for (String key : keys) {
            jedisUtil.delObject(key);
         }
    }
}
