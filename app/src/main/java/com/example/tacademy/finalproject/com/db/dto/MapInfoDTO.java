package com.example.tacademy.finalproject.com.db.dto;

/**
 * Created by Tacademy on 2016-10-17.
 */
public class MapInfoDTO {


    int i_id;//` INT(11) NOT NULL AUTO_INCREMENT,
    String i_place;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '장소명' COLLATE 'utf8_unicode_ci',
    String i_useTime;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '이용시간' COLLATE 'utf8_unicode_ci',
    String i_oldAddress;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '구주소(구)' COLLATE 'utf8_unicode_ci',
    String i_newAddress;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '새주소' COLLATE 'utf8_unicode_ci',
    double i_lati;//` DOUBLE NULL DEFAULT '0' COMMENT '위도',
    double i_longi;//` DOUBLE NULL DEFAULT '0' COMMENT '경도',
    public int getI_id() {
        return i_id;
    }
    public void setI_id(int i_id) {
        this.i_id = i_id;
    }
    public String getI_place() {
        return i_place;
    }
    public void setI_place(String i_place) {
        this.i_place = i_place;
    }
    public String getI_useTime() {
        return i_useTime;
    }
    public void setI_useTime(String i_useTime) {
        this.i_useTime = i_useTime;
    }
    public String getI_oldAddress() {
        return i_oldAddress;
    }
    public void setI_oldAddress(String i_oldAddress) {
        this.i_oldAddress = i_oldAddress;
    }
    public String getI_newAddress() {
        return i_newAddress;
    }
    public void setI_newAddress(String i_newAddress) {
        this.i_newAddress = i_newAddress;
    }
    public double getI_lati() {
        return i_lati;
    }
    public void setI_lati(double i_lati) {
        this.i_lati = i_lati;
    }
    public double getI_longi() {
        return i_longi;
    }
    public void setI_longi(double i_longi) {
        this.i_longi = i_longi;
    }
    public MapInfoDTO(int i_id, String i_place, String i_useTime, String i_oldAddress, String i_newAddress,
                      double i_lati, double i_longi) {
        super();
        this.i_id = i_id;
        this.i_place = i_place;
        this.i_useTime = i_useTime;
        this.i_oldAddress = i_oldAddress;
        this.i_newAddress = i_newAddress;
        this.i_lati = i_lati;
        this.i_longi = i_longi;
    }
    @Override
    public String toString() {
        return "MapInfoVO [i_id=" + i_id + ", i_place=" + i_place + ", i_useTime=" + i_useTime + ", i_oldAddress="
                + i_oldAddress + ", i_newAddress=" + i_newAddress + ", i_lati=" + i_lati + ", i_longi=" + i_longi + "]";
    }

    public MapInfoDTO(){}

}
