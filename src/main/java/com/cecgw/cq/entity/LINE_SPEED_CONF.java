package com.cecgw.cq.entity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "T_LINE_SPEED_CONF")
@Data
public class LINE_SPEED_CONF {
	@Id
	private Long id;
	private String start_point;
	private String terminal;
	private String start_ip;
	private String end_ip;
	private String distance;
	private int frequence;
	private int duration;
	private String scope;
	private String create_time;
	private String update_time;
	private String create_by;
	private String update_by;


	
}
