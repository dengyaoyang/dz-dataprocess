package com.cecgw.cq.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-20
 */
@Repository
public class GenericDao{

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 查询rfid中哪些数黄标
     * @param startTime
     * @param endTime
     * @return
     */
   public List<Map<String,Object>> getCurRfid(String startTime,String endTime){
//        Optional<String>  startOp = TimeUtil.covertFormat(startTime, "YYYY-MM-DD HH:MI:SS");
//        Optional<String>  endOp = TimeUtil.covertFormat(endTime, "YYYY-MM-DD HH:MI:SS");
//        String startDate = startOp.get();
//        String endDate = endOp.get();
        String sql= "select a.readerIP,a.NATURE,a.PLATE,a.VEHICLE from t_rfid_analyze_day"
                + " a  where a.c1 in ( select substr(b.hphm,2,7) from t_hb b ) and TIME > '"+startTime+"'"
                + "and '"+endTime+"'";
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 黄标车总数
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String,Object>> getCurRfidTotal(String startTime,String endTime){
//        Optional<String>  startOp = TimeUtil.covertFormat(startTime, "'YYYY-MM-DD HH24:MI:SS'");
//        Optional<String>  endOp = TimeUtil.covertFormat(endTime, "'YYYY-MM-DD HH24:MI:SS'");
//        String startDate = startOp.get();
//        String endDate = endOp.get();
        String sql = "select a.readerIP,count(a.C1)as tatol from t_rfid_analyze_day "
                + " a  where a.c1 in ( select substr(b.hphm,2,7) from t_hb b ) and TIME >= '"+startTime+"'"
                + "and TIME<= '"+endTime+"' group by a.readerIP ";
        return jdbcTemplate.queryForList(sql);
    }
}
