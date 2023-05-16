package com.cxfwork.libraryappointment.client;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReserveService {
    private Map<String,String> UserReserveInfo;
    public ReserveService(){
        UserReserveInfo = HttpService.jsonToMap(HttpService.getUserReservation());
    }
    public Map<String,String> getUserReserveInfo(){

        return UserReserveInfo;
    }
    public void signin1(){
        UserReserveInfo.replace("haveSignin1","1");
    }
    public void signin2(){
        UserReserveInfo.replace("haveSignin2","1");
    }
    public void cancel1(){
        UserReserveInfo.replace("haveReservation1","0");
    }
    public void cancel2(){
        UserReserveInfo.replace("haveReservation2","0");
    }

    /*
        ("DateID", "2");
        ("TimeIDbegin", "0");
        ("TimeIDend", "2");
        ("Building", "1");
        ("Room", "A101");
        Seat
     */

    public static List<String> getRoomsList(Map<String, String> filter){
        String[] strings = {"A101#0", "A102#1", "A103#0"};
        List<String> lists = new ArrayList<>(Arrays.asList(strings));
        if(filter.get("DateID").equals("1")){
            lists.add("A201#0");
        }
        return lists;
    }

    public static List<String> getSeatsList(Map<String, String> filter){
        //0：空闲，1：已预约，2：课程
        String[] strings = {"1#0", "2#1", "3#0"};
        List<String> lists = new ArrayList<>(Arrays.asList(strings));

        if(Objects.equals(filter.get("Room"), "A103")){
            lists.add("4#0");
        }
        if(Objects.equals(filter.get("Room"), "A201")){
            lists.add("4#1");
        }
        return lists;
    }

    public static Map<String, String> reserve(Map<String, String> filter){
        Map<String,String> result = new HashMap<>();
        result.put("status","1");
        result.put("message","Mio, 2班, 第1-4节课");
        result.put("phone","11111");
        return result;
    }
}
