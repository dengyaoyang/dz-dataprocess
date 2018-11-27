package com.cecgw.cq.repository;

import com.cecgw.cq.entity.RFID_ANALYZE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-15
 */
@Repository
public interface RfidAnalyseRep extends JpaRepository<RFID_ANALYZE,Integer>{
}
