package com.cecgw.cq.repository;

import com.cecgw.cq.entity.RFID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-15
 */
@Repository
public interface RfidRep extends JpaRepository<RFID,Integer>{

}
