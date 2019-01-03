package com.cecgw.cq.schedule;

import com.cecgw.cq.service.DeleteOriginRfidService;
import com.cecgw.cq.service.impl.DeleteOriginRfidServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author zhuguanghui
 * @Description
 * @Date 2018/11/19
 */
public class DeleteOriginRfidSchedule {

    @Autowired
    DeleteOriginRfidService deleteOriginRfidService;

    /**
     * 每天清空一次Redis origin_rfid
     */

//    @Scheduled(cron = "0 30 0 * * *" )
    public void dayTask(){
        deleteOriginRfidService.deleteData();
    }

}
