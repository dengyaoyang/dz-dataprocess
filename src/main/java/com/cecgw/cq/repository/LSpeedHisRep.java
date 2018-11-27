package com.cecgw.cq.repository;

import com.cecgw.cq.entity.LINE_SPEED_HIS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-17
 */
@Repository
public interface LSpeedHisRep extends JpaRepository<LINE_SPEED_HIS,Integer>{

}
