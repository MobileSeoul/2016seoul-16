package com.example.tacademy.finalproject.parser;

import com.example.tacademy.finalproject.com.db.dto.MapInfoListDTO;
import com.google.gson.Gson;

/**
 * Created by Tacademy on 2016-10-17.
 */
public class MapInfoParser {
    public static MapInfoListDTO parse(String jsonString){
        Gson gson = new Gson();
        MapInfoListDTO mapInfoDTO = gson.fromJson(jsonString, MapInfoListDTO.class);
        return mapInfoDTO;
    }
}
