package com.cecgw.cq.entity;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "T_LINE_SPEED_CONF")
public class LINE_SPEED_CONF {
	@Id
	private double id;
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

	/**
	 * <tt>id</tt>属性的Getter方法.
	 *
	 * @return id
	 */
	public double getId() {
		return id;
	}

	/**
	 * <tt>id</tt> 的Setter方法.
	 *
	 * @param id 成员变量的值被设置成 id
	 */
	public void setId(double id) {
		this.id = id;
	}

	public String getStart_point() {
		return start_point;
	}
	public void setStart_point(String start_point) {
		this.start_point = start_point;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getStart_ip() {
		return start_ip;
	}
	public void setStart_ip(String start_ip) {
		this.start_ip = start_ip;
	}
	public String getEnd_ip() {
		return end_ip;
	}
	public void setEnd_ip(String end_ip) {
		this.end_ip = end_ip;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * <tt>create_time</tt>属性的Getter方法.
	 *
	 * @return create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * <tt>create_time</tt> 的Setter方法.
	 *
	 * @param create_time 成员变量的值被设置成 create_time
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * <tt>update_time</tt>属性的Getter方法.
	 *
	 * @return update_time
	 */
	public String getUpdate_time() {
		return update_time;
	}

	/**
	 * <tt>update_time</tt> 的Setter方法.
	 *
	 * @param update_time 成员变量的值被设置成 update_time
	 */
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public int getFrequence() {
		return frequence;
	}
	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}
