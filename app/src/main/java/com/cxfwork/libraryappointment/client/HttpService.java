package com.cxfwork.libraryappointment.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class HttpService {
    public static Map<String, String> jsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static String mapToJson(Map<String, String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
    public static String getUserReservation(){
        String json = "{\"haveReservation1\":\"1\",\"haveSignin1\":\"0\",\"Seat1\":\"1号座位\",\"location1\":\"博学楼一楼-A101\",\"time1\":\"2023-5-12 7:00-8:00\",\"haveReservation2\":\"1\",\"haveSignin2\":\"0\",\"Seat2\":\"2号座位\",\"location2\":\"博学楼一楼-A101\",\"time2\":\"2023-5-12 8:00-9:00\"}";
        return json;
    }
}
