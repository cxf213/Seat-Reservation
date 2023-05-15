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
}
