package com.cecgw.cq.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-17
 */
@Table
@Entity(name = "T_LINE_SPEED_HIS")
@Data
public class LINE_SPEED_HIS {
    @Id
    @GeneratedValue(generator = "timeID")
    @GenericGenerator(name = "timeID", strategy = "com.cecgw.cq.util.IdGen")
    private Long line_id;
    private String speed;
    private String create_time;

}
