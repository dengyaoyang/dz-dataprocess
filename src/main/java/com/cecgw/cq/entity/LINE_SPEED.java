package com.cecgw.cq.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 路段实时速度表
 * @author 曹华鹏
 *
 */
@Table(name = "T_LINE_SPEED")
@Entity
@Data
public class LINE_SPEED {
	@Id
	@GeneratedValue(generator = "timeID")
	@GenericGenerator(name = "timeID", strategy = "com.cecgw.cq.util.IdGen")
	private Long line_id;
	private String speed;
	private String update_time;

	
}
