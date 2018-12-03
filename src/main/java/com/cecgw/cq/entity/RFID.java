package com.cecgw.cq.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RFID过车数据表
 * @author 曹华鹏
 *
 */
@Entity
@Table(name="T_RFID")
@Data
public class RFID {
	@Id
	@GeneratedValue(generator = "timeID")
	@GenericGenerator(name = "timeID", strategy = "com.cecgw.cq.util.IdGen")
	private Long id;
	private String epc;
	private String time;
	private String readerip;
	private String c1;
	private String c2;
	private String eid;
	private String color;


	
}
