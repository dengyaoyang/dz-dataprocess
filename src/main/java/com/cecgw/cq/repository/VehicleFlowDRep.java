package com.cecgw.cq.repository;

import com.cecgw.cq.entity.VEHICLE_FLOW;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by lifuyi on 2018/11/19.
 */
@Repository
public interface VehicleFlowDRep extends JpaRepository<VEHICLE_FLOW,String> {

    @Transactional
    @Modifying
    @Query(value = "delete from t_vehicle_flow_d",nativeQuery = true)
    void deleteAllData();


}
