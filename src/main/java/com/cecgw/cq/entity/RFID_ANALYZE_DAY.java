package com.cecgw.cq.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-15
 */
@Entity
@Table(name = "T_RFID_ANALYZE_DAY")
public class RFID_ANALYZE_DAY {
    @Id
    private int id;
    private String time;
    private String readerip;
    private String c1;
    private String c2;
    private String eid;
    private String color;
    private String nature;
    private String plate;
    private String vehicle;
    private String localization;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public String getNature() {
        return nature;
    }
    public void setNature(String nature) {
        this.nature = nature;
    }
    public String getPlate() {
        return plate;
    }
    public void setPlate(String plate) {
        this.plate = plate;
    }
    public String getVehicle() {
        return vehicle;
    }
    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
    public String getLocalization() {
        return localization;
    }
    public void setLocalization(String localization) {
        this.localization = localization;
    }
}
