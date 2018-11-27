package com.cecgw.cq.service.impl;

import com.cecgw.cq.entity.LINE_SPEED_CONF;
import com.cecgw.cq.repository.LSpeedConfRep;
import com.cecgw.cq.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-15
 */
@Service
public class TimeServiceImpl {
    //业务相关常量
    private static final int THREE = -3;
    @Autowired
    LSpeedConfRep lSpeedConfRep;

    public BusinessTime getTime(){
        LINE_SPEED_CONF lineSpeedConf = lSpeedConfRep.getOne(1);
        Integer duration = lineSpeedConf.getDuration();
        LocalDateTime threeLocalDate = TimeUtil.beforeThree(THREE);
        LocalDateTime durationLocalDate = TimeUtil.beforeThree(duration);
        String endDate = threeLocalDate.format(TimeUtil.FUALLDATE);
        String starDate = durationLocalDate.format(TimeUtil.FUALLDATE);
        //返回开始时间天数
        String day = starDate.substring(8,10);
        String scope = "00:00-23:59";
        String[] scopeArray = scope.split("-");
        String scopeStart = scopeArray[0];
        String scopeEnd = scopeArray[1];
        //返回开始日期的  时间部分
        String startTime = starDate.substring(11,19);
        //返回结束日期的  时间部分
        String endTime = endDate.substring(11,19);
        //当前时间 只含年月 Unable to obtain LocalTime from TemporalAccessor:
        String currentDate = LocalDateTime.now().format(TimeUtil.YM);
        //根据业务 拼接字符串..  保留以前逻辑不做修改  得到一个真正使用的起始时间
        LocalDateTime beginDate = LocalDateTime.parse(currentDate+"-"+day+" "+scopeStart+":00",TimeUtil.FUALLDATE);
        //根据业务 拼接字符串..  保留以前逻辑不做修改  得到一个真正使用的结束时间
        LocalDateTime overDate = LocalDateTime.parse(currentDate+"-"+day+" "+scopeEnd+":00", TimeUtil.FUALLDATE);
        //返回业务时间类
        return new BusinessTime(beginDate,overDate,threeLocalDate,durationLocalDate);
    }

    public static void main(String[] args) {
        LocalDateTime threeLocalDate = TimeUtil.beforeThree(3);
        LocalDateTime durationLocalDate = TimeUtil.beforeThree(10);
        String endDate = threeLocalDate.format(TimeUtil.FUALLDATE);
        String starDate = durationLocalDate.format(TimeUtil.FUALLDATE);
        //返回开始时间天数
        String day = starDate.substring(8,10);
        String scope = "00:00-23:59";
        String[] scopeArray = scope.split("-");
        String scopeStart = scopeArray[0];
        String scopeEnd = scopeArray[1];
        //返回开始日期的  时间部分
        String startTime = starDate.substring(11,19);
        //返回结束日期的  时间部分
        String endTime = endDate.substring(11,19);
        //当前时间 只含年月 Unable to obtain LocalTime from TemporalAccessor:
        String currentDate = LocalDateTime.now().format(TimeUtil.YM);
        //根据业务 拼接字符串..  保留以前逻辑不做修改  得到一个真正使用的起始时间
        LocalDateTime beginDate = LocalDateTime.parse(currentDate+"-"+day+" "+scopeStart+":00",TimeUtil.FUALLDATE);
        //根据业务 拼接字符串..  保留以前逻辑不做修改  得到一个真正使用的结束时间
        LocalDateTime overDate = LocalDateTime.parse(currentDate+"-"+day+" "+scopeEnd+":00", TimeUtil.FUALLDATE);
        System.out.println(beginDate);
        System.out.println(overDate);
    }

    public static class BusinessTime{

        public BusinessTime(LocalDateTime beginDate, LocalDateTime overDate, LocalDateTime threeLocalDate, LocalDateTime durationLocalDate) {
            this.beginDate = beginDate;
            this.overDate = overDate;
            this.threeLocalDate = threeLocalDate;
            this.durationLocalDate = durationLocalDate;
        }

        /**
         * <tt>beginDate</tt>属性的Getter方法.
         *
         * @return beginDate
         */
        public LocalDateTime getBeginDate() {
            return beginDate;
        }

        /**
         * <tt>beginDate</tt> 的Setter方法.
         *
         * @param beginDate 成员变量的值被设置成 beginDate
         */
        public void setBeginDate(LocalDateTime beginDate) {
            this.beginDate = beginDate;
        }

        /**
         * <tt>overDate</tt>属性的Getter方法.
         *
         * @return overDate
         */
        public LocalDateTime getOverDate() {
            return overDate;
        }

        /**
         * <tt>overDate</tt> 的Setter方法.
         *
         * @param overDate 成员变量的值被设置成 overDate
         */
        public void setOverDate(LocalDateTime overDate) {
            this.overDate = overDate;
        }

        /**
         * <tt>threeLocalDate</tt>属性的Getter方法.
         *
         * @return threeLocalDate
         */
        public LocalDateTime getThreeLocalDate() {
            return threeLocalDate;
        }

        /**
         * <tt>threeLocalDate</tt> 的Setter方法.
         *
         * @param threeLocalDate 成员变量的值被设置成 threeLocalDate
         */
        public void setThreeLocalDate(LocalDateTime threeLocalDate) {
            this.threeLocalDate = threeLocalDate;
        }

        /**
         * <tt>durationLocalDate</tt>属性的Getter方法.
         *
         * @return durationLocalDate
         */
        public LocalDateTime getDurationLocalDate() {
            return durationLocalDate;
        }

        /**
         * <tt>durationLocalDate</tt> 的Setter方法.
         *
         * @param durationLocalDate 成员变量的值被设置成 durationLocalDate
         */
        public void setDurationLocalDate(LocalDateTime durationLocalDate) {
            this.durationLocalDate = durationLocalDate;
        }

        LocalDateTime beginDate;
        LocalDateTime overDate;
        LocalDateTime threeLocalDate;
        LocalDateTime durationLocalDate;

    }
}

