package com.cecgw.cq.service;

/**
 * Created by lifuyi on 2018/11/19.
 */
public interface ReaderVehicleFlowCountService {

    void doVehicleFlowCount(String indexName, String redisKey, long time, long time1, String dbUpdateTime, String tVehicleFlowD, String weekDay, boolean b);
}
