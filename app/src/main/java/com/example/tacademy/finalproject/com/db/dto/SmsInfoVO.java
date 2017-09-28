package com.example.tacademy.finalproject.com.db.dto;

/**
 * Created by Tacademy on 2016-10-25.
 */
public class SmsInfoVO {
    int s_id;//` INT(11) NOT NULL AUTO_INCREMENT,
    String s_location;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '위치' COLLATE 'utf8_unicode_ci',
    String s_pw;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '비밀번호' COLLATE 'utf8_unicode_ci',
    String s_receive;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '수신자' COLLATE 'utf8_unicode_ci',
    String s_delivery;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '배송자' COLLATE 'utf8_unicode_ci',
    String s_user;//` VARCHAR(50) NULL DEFAULT '' COMMENT '고유번호' COLLATE 'utf8_unicode_ci',
    String s_memo;//` VARCHAR(50) NOT NULL DEFAULT ' ' COMMENT '메모' COLLATE 'utf8_unicode_ci',
    String s_link;//` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '링크' COLLATE 'utf8_unicode_ci',
    String s_regi;//` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '날짜',

    public SmsInfoVO(){}

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getS_location() {
        return s_location;
    }

    public void setS_location(String s_location) {
        this.s_location = s_location;
    }

    public String getS_pw() {
        return s_pw;
    }

    public void setS_pw(String s_pw) {
        this.s_pw = s_pw;
    }

    public String getS_receive() {
        return s_receive;
    }

    public void setS_receive(String s_receive) {
        this.s_receive = s_receive;
    }

    public String getS_delivery() {
        return s_delivery;
    }

    public void setS_delivery(String s_delivery) {
        this.s_delivery = s_delivery;
    }

    public String getS_user() {
        return s_user;
    }

    public void setS_user(String s_user) {
        this.s_user = s_user;
    }

    public String getS_memo() {
        return s_memo;
    }

    public void setS_memo(String s_memo) {
        this.s_memo = s_memo;
    }

    public String getS_link() {
        return s_link;
    }

    public void setS_link(String s_link) {
        this.s_link = s_link;
    }

    public String getS_regi() {
        return s_regi;
    }

    public void setS_regi(String s_regi) {
        this.s_regi = s_regi;
    }

    @Override
    public String toString() {
        return "SmsInfoVO{" +
                "s_id=" + s_id +
                ", s_location='" + s_location + '\'' +
                ", s_pw='" + s_pw + '\'' +
                ", s_receive='" + s_receive + '\'' +
                ", s_delivery='" + s_delivery + '\'' +
                ", s_user='" + s_user + '\'' +
                ", s_memo='" + s_memo + '\'' +
                ", s_link='" + s_link + '\'' +
                ", s_regi='" + s_regi + '\'' +
                '}';
    }
}
