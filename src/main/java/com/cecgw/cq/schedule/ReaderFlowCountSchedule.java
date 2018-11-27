package com.cecgw.cq.schedule;

import com.cecgw.cq.entity.TrafficFlow;
import com.cecgw.cq.service.ReaderVehicleFlowCountService;
import com.cecgw.cq.util.ESUtil;
import com.cecgw.cq.util.TimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lifuyi on 2018/11/19.
 * @author lifuyi
 */
@Component
public class ReaderFlowCountSchedule {

    @Autowired
    private ReaderVehicleFlowCountService readerVehicleFlowCountService;

    private Log logger = LogFactory.getLog(this.getClass());

    /**
     * 分车型统计的车流量------5min(最小时间)
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void calVehicleFlowCount5min(){
        //此时此刻
        Calendar current = Calendar.getInstance();
        String weekDay = TimeUtil.getWeek(current);
        //得到date对象
        Date end = current.getTime();

        //2018-02-02
        String dateStr =  TimeUtil.formatDate(end,"yyyy-MM-dd");
        //2018-02-02 00:00:00
        String dbUpdateTime = TimeUtil.formatDate(end,"yyyy-MM-dd HH:mm:ss");

        String indexName = ESUtil.getIndexName(dateStr);
        current.add(Calendar.MINUTE, -5);
        Date start = current.getTime();
        System.out.println(start+"==========="+end);
        long currentMi = System.currentTimeMillis();

        readerVehicleFlowCountService.doVehicleFlowCount(indexName,"AfterVehicle", start.getTime(), end.getTime(), dbUpdateTime,
                TrafficFlow.T_VEHICLE_FLOW_D, weekDay,false);
        long afterMi = System.currentTimeMillis();
        System.out.println("分车型流量5min计算耗时:" + ((afterMi - currentMi) / 1000) + "秒");
    }

    /**
     * 分车型统计的车流量------1h
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void calVehicleFlowCount1h(){
        //此时此刻(比如00:xx:xx)
        Calendar current = Calendar.getInstance();
        String weekDay = TimeUtil.getWeek(current);
        //得到date对象
        Date end = current.getTime();

        String dateStr = TimeUtil.formatDate(end,"yyyy-MM-dd");
        String dbUpdateTime =TimeUtil.formatDate(end,"yyyy-MM-dd HH:mm:ss");
        String indexName = ESUtil.getIndexName(dateStr);

        //算前一个小时的，所以减去一个小时
        //比如23:xx:xx
        current.add(Calendar.HOUR_OF_DAY, -1);
        Date start = current.getTime();

        long currentMi = System.currentTimeMillis();
        readerVehicleFlowCountService.doVehicleFlowCount(indexName, "AfterVehicle", start.getTime(), end.getTime(), dbUpdateTime,
                TrafficFlow.T_VEHICLE_FLOW_His_HOUR, weekDay,false);
        long afterMi = System.currentTimeMillis();
        System.out.println("分车型流量1h计算耗时:" + ((afterMi - currentMi) / 1000) + "秒");
    }

    /**
     * 分车型统计的车流量------0点
     */
    @Scheduled(cron = "59 59 23 * * ?")
    public void doVehicleFlowCount1day(){
        //此时此刻(比如00:xx:xx)
        Calendar current = Calendar.getInstance();
        String weekDay = TimeUtil.getWeek(current);
        //得到date对象
        Date end = current.getTime();

        String dateStr = TimeUtil.formatDate(end,"yyyy-MM-dd");
        String dbUpdateTime =TimeUtil.formatDate(end,"yyyy-MM-dd HH:mm:ss");
        String indexName = ESUtil.getIndexName(dateStr);

        //算前一个小时的，所以减去一个小时
        //加1秒
        current.add(Calendar.SECOND,1);
        //减一天
        current.add(Calendar.DAY_OF_MONTH, -1);
        Date start = current.getTime();

        long currentMi = System.currentTimeMillis();
        readerVehicleFlowCountService.doVehicleFlowCount(indexName, "AfterVehicle", start.getTime(), end.getTime(), dbUpdateTime,
                TrafficFlow.T_VEHICLE_FLOW_His_DAY, weekDay,true);
        long afterMi = System.currentTimeMillis();
        System.out.println("分车型流量1h计算耗时:" + ((afterMi - currentMi) / 1000) + "秒");
    }

}
