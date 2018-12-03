package com.cecgw.cq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lifuyi on 2018/11/20.
 */
@Entity
@Table(name = "T_RFID_STATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RFID_STATION {
    @Id
    private String readerip;
    private String name;
    private String direction;
}
