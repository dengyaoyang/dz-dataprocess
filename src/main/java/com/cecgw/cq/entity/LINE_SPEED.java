package com.cecgw.cq.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 路段实时速度表
 * @author 曹华鹏
 *
 */
@Table(name = "T_LINE_SPEED")
@Entity
public class LINE_SPEED {
	@Id
	private double line_id;
	private String speed;
	private String update_time;

	/**
	 * <tt>line_id</tt>属性的Getter方法.
	 *
	 * @return line_id
	 */
	public double getLine_id() {
		return line_id;
	}

	/**
	 * <tt>line_id</tt> 的Setter方法.
	 *
	 * @param line_id 成员变量的值被设置成 line_id
	 */
	public void setLine_id(double line_id) {
		this.line_id = line_id;
	}

	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
}
