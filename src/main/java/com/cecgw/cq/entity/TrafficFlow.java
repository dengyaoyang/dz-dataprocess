package com.cecgw.cq.entity;

/**
 * Created by lifuyi on 2018/11/19.
 * @Auther:lifuyi
 * @Description:实时中的一些配置表
 */
public class TrafficFlow {
    /**
     * 车型流量统计实时统计表（D）
     */
    public static String T_VEHICLE_FLOW_D="t_vehicle_flow_d";
    /**
     * 车型流量历史统计表（天维度表）
     */
    public static String T_VEHICLE_FLOW_His_DAY="t_vehicle_flow_his_day";
    /**
     * T_RFID_ANALYZE_DAY过车解析表(只保存一天的数据)
     */
    public static String RFID_AnalyzeTablename="t_rfid_analyze_day";
    /**
     * 车型流量历史统计表（小时表）
     */
    public static String T_VEHICLE_FLOW_His_HOUR="t_vehicle_flow_his_hour";
    /**
     * 车型流量5min表
     */
    public static String T_VEHICLE_FLOW_HIS_5MIN="t_vehicle_flow_his_5min";
}
