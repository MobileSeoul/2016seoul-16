package com.example.tacademy.finalproject.com.db.dto;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2016-10-25.
 */
public class SmsInfoListVO {
    String result;
    ArrayList<SmsInfoVO> listVO;

    public SmsInfoListVO(){}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<SmsInfoVO> getListVO() {
        return listVO;
    }

    public void setListVO(ArrayList<SmsInfoVO> listVO) {
        this.listVO = listVO;
    }

    @Override
    public String toString() {
        return "SmsInfoListVO{" +
                "result='" + result + '\'' +
                ", listVO=" + listVO +
                '}';
    }
}
