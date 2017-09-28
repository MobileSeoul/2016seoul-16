package com.example.tacademy.finalproject.com.db.dto;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2016-10-17.
 */
public class MapInfoListDTO {

    private String result;
    private ArrayList<MapInfoDTO> listVO;

    public MapInfoListDTO(){}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<MapInfoDTO> getListVO() {
        return listVO;
    }

    public void setListVO(ArrayList<MapInfoDTO> listVO) {
        this.listVO = listVO;
    }

    @Override
    public String toString() {
        return "MapInfoListDTO{" +
                "result='" + result + '\'' +
                ", listVO=" + listVO +
                '}';
    }

    public MapInfoListDTO(String result, ArrayList<MapInfoDTO> listVO) {
        this.result = result;
        this.listVO = listVO;
    }
}
