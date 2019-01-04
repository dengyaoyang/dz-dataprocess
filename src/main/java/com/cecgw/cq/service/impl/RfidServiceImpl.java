package com.cecgw.cq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cecgw.cq.entity.LINE_SPEED;
import com.cecgw.cq.entity.LINE_SPEED_CONF;
import com.cecgw.cq.entity.LINE_SPEED_HIS;
import com.cecgw.cq.entity.RFID_ANALYZE;
import com.cecgw.cq.repository.LSpeedConfRep;
import com.cecgw.cq.repository.LSpeedHisRep;
import com.cecgw.cq.repository.LSpeedRep;
import com.cecgw.cq.service.RfidService;
import com.cecgw.cq.util.JedisUtil;
import com.cecgw.cq.util.TimeUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-15
 */
@Service
public class RfidServiceImpl implements RfidService{

    @Autowired
    LSpeedConfRep lSpeedConfRep;
    @Autowired
    LSpeedHisRep lSpeedHisRep;
    @Autowired
    LSpeedRep lSpeedRep;
    @Autowired
    JedisUtil jedisUtil;
    /**
     * redis里面记录的均为符合计算条件的的过车数据
     * 这些数据包含  经过起点等过车数据和经过终点等过车数据
     * 计算车流速度  然后把拆分后到过车数据 归集到多张表中
     */
    public void calculateSpeed() {
        List<String> startRifdJ = jedisUtil.getList("startRfid");
        List<String> endRifdJ = jedisUtil.getList("endRfid");
        List<String> confJ = jedisUtil.getList("speedConf");
        List<Double> speedList = Lists.newArrayList();
        //Json转对象
        List<RFID_ANALYZE> startRfid = JSONArray.parseArray(startRifdJ.toString(), RFID_ANALYZE.class);
        List<RFID_ANALYZE> endRfid = JSONArray.parseArray(endRifdJ.toString(),RFID_ANALYZE.class);
        List<LINE_SPEED_CONF> conf = JSONArray.parseArray(confJ.toString(),LINE_SPEED_CONF.class);
        Double sumSpeed = 0.0;
        //平均速度
        Double avg = 0.0;
        //平方速度
        Double pro = 0.0;
        //方差
        Double fc = 0.0;
        //最终计算好以后的速度集合
        List<Double> resultSpdList = Lists.newArrayList();
        Double resultSumSpeed = 0.0;
        Double resultAvg = 0.0;
        String sJson = "";
        String eJson = "";
        String currentTime = LocalDateTime.now().format(TimeUtil.FUALLDATE).toString();
        for (int j=0;j<conf.size();j++) {
            //kafka 取数过滤逻辑 起始和结束的集合应该数一样多循环任意皆可
            try {
                for (int i = 0; i < endRfid.size(); i++) {
                    RFID_ANALYZE endRfidObj = endRfid.get(i);
//                    eJson = JSON.toJSONString(endRfidObj);
                    //起点ip和终点ip进行配对
                    if (!startRfid.stream().anyMatch(e->e.getEid().equals(endRfidObj.getEid()))){
                        continue;
                    }
                    RFID_ANALYZE startRfidObj = startRfid.stream()
                                                         .filter(e->e.getEid().equals(endRfidObj.getEid()))
                                                         .findAny().get();
//                    sJson = JSON.toJSONString(startRfidObj);
                    //取到了就删除Redis中已得到的开始和结束数据
//                    jedisUtil.delListVal("startRfid",sJson);
//                    jedisUtil.delListVal("endRfid",eJson);
                    //                String startStr = TimeUtil.formatDate(startRfidObj.getTime(),TimeUtil.FULL_CODE);
                    //                String endStr = TimeUtil.formatDate(endRfid.get(i).getTime(),TimeUtil.FULL_CODE);
                    Date startDate = startRfidObj.getTime();
                    Date endDate = endRfid.get(i).getTime();
                    //判断2个时间发生在同一天
                    if (TimeUtil.getDayOfMouth(startDate) == TimeUtil.getDayOfMouth(endDate)) {
                        //判断结束时间是否大于起始时间
                        if (endDate.getTime() > startDate.getTime()) {
                            long timeDiff = endDate.getTime() - startDate.getTime();
                            //根据时间差算速度.
                            double speed = Double.parseDouble(conf.get(j).getDistance())/(timeDiff);
                            BigDecimal bigDecimal = new BigDecimal(speed);
                            speedList.add(bigDecimal.setScale(2,BigDecimal.ROUND_DOWN).doubleValue());
                        }
                    }
                }
                if (speedList.size()>0){
                    sumSpeed = speedList.stream().reduce((sum,indexVal)->sum+indexVal).get();
                    avg = sumSpeed/speedList.size();
                    Double finalAvg = avg;
                    pro = speedList.stream().map(n->Math.pow(n- finalAvg, 2)).reduce((x, y)->x+y).get();
                    fc =Math.sqrt(pro/speedList.size()-1);
                    double max = avg+fc;
                    double vmax = (max<200)?max:200;
                    double vmin = avg-fc;
                    for(double speed:speedList){
                        if(speed>=vmin && speed<=vmax){
                            resultSpdList.add(speed);
                        }else if(speedList.size()==1){
                            resultSpdList.add(speed);
                        }
                    }
                    Optional optional =  resultSpdList.stream().reduce((x,y)->x+y);
                    if (optional.isPresent()) {
                        resultSumSpeed = resultSpdList.stream().reduce((x,y)->x+y).get();
                    }
                    if (speedList.size()>0){
                        resultAvg = resultSumSpeed/resultSpdList.size();
                    }
                    //插入速度表。。。。。。todo
                    LINE_SPEED line_speed = new LINE_SPEED();
                    line_speed.setLine_id(conf.get(j).getId());
                    LINE_SPEED_HIS lineSpeedHis = new LINE_SPEED_HIS();
                    lineSpeedHis.setLine_id(conf.get(j).getId());
                    lineSpeedHis.setCreate_time (currentTime);
                    if (resultAvg.isNaN()){
                        line_speed.setSpeed("-1");
                        lineSpeedHis.setSpeed("-1");
                    }else {
                        lineSpeedHis.setSpeed(String.valueOf(resultAvg));
                        line_speed.setSpeed(String.valueOf(resultAvg));
                    }
                    line_speed.setUpdate_time(currentTime);
                    lSpeedRep.save(line_speed);
                    lSpeedHisRep.save(lineSpeedHis);
                }
            }catch (RuntimeException e){
                LINE_SPEED line_speed = new LINE_SPEED();
                line_speed.setLine_id(conf.get(j).getId());
                line_speed.setSpeed("-1");
                line_speed.setUpdate_time(currentTime);
                LINE_SPEED_HIS lineSpeedHis = new LINE_SPEED_HIS();
                lineSpeedHis.setLine_id(conf.get(j).getId());
                lineSpeedHis.setSpeed("-1");
                lineSpeedHis.setCreate_time (currentTime);
                lSpeedRep.save(line_speed);
                lSpeedHisRep.save(lineSpeedHis);
            }

        }


    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(5.843776378157262E-7);
        System.out.println(bigDecimal.toPlainString());
        System.out.println(bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
        List<Double> list = new ArrayList();
        if (list.stream().reduce((x, y) -> x + y).isPresent()){
            System.out.println(list.stream().reduce((x, y) -> x + y).get());
        }
    }
}
