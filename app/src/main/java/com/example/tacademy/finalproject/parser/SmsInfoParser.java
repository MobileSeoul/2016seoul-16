package com.example.tacademy.finalproject.parser;

import com.example.tacademy.finalproject.com.db.dto.SmsInfoListVO;
import com.google.gson.Gson;

/**
 * Created by Tacademy on 2016-10-25.
 */
public class SmsInfoParser {
    public static SmsInfoListVO parse(String jsonString){

        Gson gson = new Gson();
        SmsInfoListVO smsInfoListVO = gson.fromJson(jsonString, SmsInfoListVO.class);

        return smsInfoListVO;
    }
}
