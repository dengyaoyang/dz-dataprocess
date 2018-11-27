package com.cecgw.cq.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RFID过车数据表
 * @author 曹华鹏
 *
 */
@Entity
@Table(name="T_RFID")
public class RFID {
	@Id
	private int id;
	private String epc;
	private String time;
	private String readerip;
	private String c1;
	private String c2;
	private String eid;
	private String color;
	
	public RFID() {
		// TODO Auto-generated constructor stub
	}
	
	public RFID(int id, String epc, String time, String readerip, String c1, String c2, String eid, String color) {
		this.id = id;
		this.epc = epc;
		this.time = time;
		this.readerip = readerip;
		this.c1 = c1;
		this.c2 = c2;
		this.eid = eid;
		this.color = color;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getReaderip() {
		return readerip;
	}
	public void setReaderip(String readerip) {
		this.readerip = readerip;
	}
	public String getC1() {
		return c1;
	}
	public void setC1(String c1) {
		this.c1 = c1;
	}
	public String getC2() {
		return c2;
	}
	public void setC2(String c2) {
		this.c2 = c2;
	}
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
