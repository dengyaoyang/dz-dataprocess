package com.cecgw.cq.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lifuyi on 2018/11/20.
 */
@Entity
@Table(name = "T_RFID_STATION")
public class RFID_STATION {
    @Id
    private String readerip;
    private String name;
    private String direction;

    public String getReaderip() {
        return readerip;
    }

    public void setReaderip(String readerip) {
        this.readerip = readerip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public RFID_STATION(String readerip, String name, String direction) {
        this.readerip = readerip;
        this.name = name;
        this.direction = direction;
    }

    public RFID_STATION() {
    }
}
