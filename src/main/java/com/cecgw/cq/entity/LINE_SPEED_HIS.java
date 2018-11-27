package com.cecgw.cq.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-17
 */
@Table
@Entity(name = "T_LINE_SPEED_HIS")
public class LINE_SPEED_HIS {
    @Id
    private double line_id;

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

    private String speed;
    private String create_time;



    public String getCreate_time() {
        return create_time;
    }
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }

}
