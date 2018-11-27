package com.cecgw.cq.schedule;

import com.cecgw.cq.service.RfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-18
 */
@Component
public class SpeedSchedule {
    @Autowired
    RfidService rfidService;

//    @Scheduled(cron = "0/2 * * * * ? ")
    public void speedTask(){
        rfidService.calculateSpeed();
    }
}
