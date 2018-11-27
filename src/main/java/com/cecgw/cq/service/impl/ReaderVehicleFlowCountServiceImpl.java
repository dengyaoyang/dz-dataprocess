package com.cecgw.cq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cecgw.cq.entity.*;
import com.cecgw.cq.pojo.EPC_DICT;
import com.cecgw.cq.repository.VehicleFlowDRep;
import com.cecgw.cq.service.ReaderVehicleFlowCountService;
import com.cecgw.cq.util.JedisUtil;
import com.cecgw.cq.util.TimeUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lifuyi on 2018/11/19.
 *
 * @author lifuyi
 */
@Service
public class ReaderVehicleFlowCountServiceImpl implements ReaderVehicleFlowCountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VehicleFlowDRep vehicleFlowDRep;
    @Autowired
    private TransportClient client;

    @Autowired
    JedisUtil jedisUtil;

    @Override
    public void doVehicleFlowCount(String indexName, String typeName, long startTime, long endTime,
                                   String updateTime, String tableName, String weekDay, boolean isLastHour) {
        //删除(实时表)前一周期数据
        if (TrafficFlow.T_VEHICLE_FLOW_D.equals(tableName)) {
            vehicleFlowDRep.deleteAllData();
        }
        /**
         * 计算当前周期的车流量
         */
        docount(indexName, typeName, startTime, endTime, updateTime, tableName, weekDay, isLastHour);
    }

    private void docount(String indexName, String typeName, long startTime, long endTime, String updateTime,
                         String tableName, String weekDay, boolean isLastHour) {
        BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("passTime").from(startTime).to(endTime);
        //查询条件：大足区，指定时间段内的
        rootQuery = rootQuery.must(queryBuilder).must(QueryBuilders.wildcardQuery("cjdid", "DZQ*"));

        // 构造嵌套聚合(相当于先按ip分组)
        AggregationBuilder ipAggs = AggregationBuilders.terms("ip_aggs").field("readerIP").size(Integer.MAX_VALUE);
        //使用类型
        AggregationBuilder vehicleAggs = AggregationBuilders.terms("vehicle_aggs").field("epcVehicleCode")
                .size(Integer.MAX_VALUE);
        //车牌类型
        AggregationBuilder plateAggs = AggregationBuilders.terms("plate_aggs").field("epcPlateCode")
                .size(Integer.MAX_VALUE);
        //营运类型
        AggregationBuilder natureAggs = AggregationBuilders.terms("nature_aggs").field("epcNatureCode")
                .size(Integer.MAX_VALUE);
        ipAggs.subAggregation(vehicleAggs);
        ipAggs.subAggregation(plateAggs);
        ipAggs.subAggregation(natureAggs);


        SearchResponse searchResponse = client.prepareSearch(indexName).setTypes(typeName).setQuery(rootQuery).
                addAggregation(ipAggs).setExplain(true).execute().actionGet();

        //readerIp聚合
        Terms groupCode = searchResponse.getAggregations().get("ip_aggs");
        // 查询类型字典表
        List<String> epcStrList = jedisUtil.getList(RedisKey.T_EPC_DICT);
        List<EPC_DICT> epcDictList = new ArrayList<>();
        JSONObject jsonObject;
        for (String epcStr : epcStrList) {
            jsonObject = JSON.parseObject(epcStr);
            EPC_DICT epcDict = new EPC_DICT((String) jsonObject.get("code"), (String) jsonObject.get("type"),
                    (String) jsonObject.get("sub_type"), (String) jsonObject.get("description"));
            epcDictList.add(epcDict);
        }
        //查询rfid设备字典表
        List<String> tRfidStationStr = jedisUtil.getList(RedisKey.T_RFID_STATION);
        List<RFID_STATION> rfidStationList = new ArrayList<>();
        for (String str : tRfidStationStr) {
            jsonObject = JSON.parseObject(str);
            RFID_STATION rfidStation = new RFID_STATION((String) jsonObject.get("readerip"),
                    (String) jsonObject.get("name"), (String) jsonObject.get("direction"));
            rfidStationList.add(rfidStation);
        }
        List<? extends Terms.Bucket> buckets = groupCode.getBuckets();
        //对每一个readerip进行处理，查找每个类型的车流量
        for (RFID_STATION rfidStation : rfidStationList) {
            VEHICLE_FLOW insertVehicleFlow = new VEHICLE_FLOW();
            try {
                insertVehicleFlow.setUpdate_time(TimeUtil.getDate(updateTime, "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            insertVehicleFlow.setRfidip(rfidStation.getReaderip());
            insertVehicleFlow.setWork_day(weekDay);
            for (Terms.Bucket bucket : buckets) {
                //过滤大足数据
                if (bucket.getKeyAsString().equals(rfidStation.getReaderip())) {
                    //总流量
                    insertVehicleFlow.setTatol((int) bucket.getDocCount());
                    // 获取子聚合
                    Aggregations children = bucket.getAggregations();
                    Terms vehicleGroup = children.get("vehicle_aggs");
                    Terms plateGroup = children.get("plate_aggs");
                    Terms natureGroup = children.get("nature_aggs");
                    List<? extends Terms.Bucket> vehicleBucket = vehicleGroup.getBuckets();
                    List<? extends Terms.Bucket> plateBucket = plateGroup.getBuckets();
                    List<? extends Terms.Bucket> natureBucket = natureGroup.getBuckets();
                    //匹配车辆类型的sub_type，并计数
                    outer:
                    for (EPC_DICT epcDict : epcDictList) {
                        String type = epcDict.getType();
                        String subType = epcDict.getSub_type();
                        String code = epcDict.getCode();
                        //反射实体类寻找set方法
                        Method[] methods = insertVehicleFlow.getClass().getMethods();
                        int methodIndex = 0;
                        for (int i = 0; i < methods.length; i++) {
                            if (("set" + code).toLowerCase().equals(methods[i].getName().toLowerCase())) {
                                methodIndex = i;
                            }
                        }


                        if ("VEHICLE".equals(type)) {
                            for (Terms.Bucket vehicle : vehicleBucket) {
                                if (subType.equals(vehicle.getKeyAsString())) {
                                    try {
                                        methods[methodIndex].invoke(insertVehicleFlow, (int) vehicle.getDocCount());
                                        continue outer;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else if ("NATURE".equals(type)) {
                            for (Terms.Bucket nature : natureBucket) {
                                if (subType.equals(nature.getKeyAsString())) {
                                    try {
                                        methods[methodIndex].invoke(insertVehicleFlow, (int) nature.getDocCount());
                                        continue outer;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else if ("PLATE".equals(type)) {
                            for (Terms.Bucket plate : plateBucket) {
                                if (subType.equals(plate.getKeyAsString())) {
                                    try {
                                        methods[methodIndex].invoke(insertVehicleFlow, (int) plate.getDocCount());
                                        continue outer;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
            //保存数据
            //判断表名
            if (TrafficFlow.T_VEHICLE_FLOW_D.equals(tableName)) {
                vehicleFlowDRep.save(insertVehicleFlow);
            } else if (TrafficFlow.T_VEHICLE_FLOW_His_DAY.equals(tableName) || TrafficFlow.T_VEHICLE_FLOW_His_HOUR.equals(tableName)) {
                StringBuilder sqlRoot = new StringBuilder("insert into ");
                StringBuilder sqlColumn = new StringBuilder();
                StringBuilder sqlValue = new StringBuilder();
                sqlRoot = sqlRoot.append(tableName).append(" (");
                //反射
                try {
                    Field[] declaredFields = insertVehicleFlow.getClass().getDeclaredFields();
                    for (int i = 0; i < declaredFields.length; i++) {
                        declaredFields[i].setAccessible(true);
                        if ("update_time".equals(declaredFields[i].getName())) {
                            sqlColumn.append("create_time").append(",");
                            String createTime="to_date('"+
                                    TimeUtil.formatDate((Date) declaredFields[i].get(insertVehicleFlow), "yyyy-MM-dd HH:mm:ss")+
                                    "','YYYY-MM-DD HH24:MI:SS') ";
                            sqlValue.append(createTime).append(",");
                        } else {
                            sqlColumn.append(declaredFields[i].getName()).append(",");
                            sqlValue.append("'").append(declaredFields[i].get(insertVehicleFlow)).append("',");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sqlColumn = sqlColumn.deleteCharAt(sqlColumn.length() - 1);
                sqlValue = sqlValue.deleteCharAt(sqlValue.length() - 1);
                sqlRoot = sqlRoot.append(sqlColumn).append(")").append(" values(").append(sqlValue).append(")");
                System.out.println("sql语句：" + sqlRoot.toString());
                jdbcTemplate.execute(sqlRoot.toString());
            }
            System.out.println("xjsiacmskcmkdsc");
        }
        System.out.println("--------------------------");
        //插入5分钟表
        if (tableName.equals(TrafficFlow.T_VEHICLE_FLOW_D)) {
            jdbcTemplate.execute("insert into "+ TrafficFlow.T_VEHICLE_FLOW_HIS_5MIN+" select * from " + TrafficFlow.T_VEHICLE_FLOW_D);
        }
        // 如果现在是0点就要清空解析天表
        if (TrafficFlow.T_VEHICLE_FLOW_His_DAY.equals(tableName) && isLastHour) {
            jdbcTemplate.execute("delete from " + TrafficFlow.RFID_AnalyzeTablename);
        }

    }
}
