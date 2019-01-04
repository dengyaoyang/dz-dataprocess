package com.cecgw.cq.schedule;

import com.cecgw.cq.repository.LSpeedRep;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author denghualin
 * @version V1.0
 * @since 2019-01-03
 *
 * 暂时不启用
 */
//@Component
public class DeleteSpeedTab {
    @Autowired
    LSpeedRep lSpeedRep;

    //cron 根据具体情况修改
    //@Scheduled(cron = "0 30 0 * * *" )
    public void delTask(){
        lSpeedRep.deleteAll();
    }
}
