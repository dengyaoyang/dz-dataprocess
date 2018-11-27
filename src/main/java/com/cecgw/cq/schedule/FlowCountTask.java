package com.cecgw.cq.schedule;

import com.cecgw.cq.service.FlowCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-20
 */
@Component
public class FlowCountTask {

    @Autowired
    FlowCountService flowCountService;
    /**
     * 黄标车流量统计 －－－5min
     */
//    @Scheduled(cron = "0 0/5 * * * *")
    public void fiveMinFlowTask(){
        flowCountService.doyellowcount();
    }
}
