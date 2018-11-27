package com.cecgw.cq.repository;

import com.cecgw.cq.entity.RFID_ANALYZE_DAY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-15
 */
@Repository
public interface RfidAnalyseDayRep extends JpaRepository<RFID_ANALYZE_DAY,Integer>{
    /**
     * 通过ip和时间范围查询
     * @param ip
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "select id,time,readerip,c1,c2,eid,color from T_RFID_ANALYZE_DAY WHERE readerip=:ip " +
                   " and to_date(substr(time,0,19),'yyyy-MM-dd hh24:mi:ss') between :startTime and :endTime",nativeQuery = true)
    List<RFID_ANALYZE_DAY> queryCacheIpAndTime(@Param("ip") String ip, @Param("startTime") String startTime,
                                                @Param("endTime") String endTime);

    @Query(value = "select id,time,readerip,c1,c2,eid,color from T_RFID_ANALYZE_DAY WHERE c1=:c1 and readerip=:ip " +
                   "and to_date(substr(time,0,19),'yyyy-MM-dd hh24:mi:ss') between :startTime and :endTime",nativeQuery = true)
    List<RFID_ANALYZE_DAY> queryCacheCOneAndTime(@Param("ip") String ip,@Param("c1")String c1,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime);

}
