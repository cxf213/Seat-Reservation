package com.cxfwork.libraryappointment.client;


import java.util.Map;

public class ReserveService {
    private Map<String,String> UserReserveInfo;
    public ReserveService(){
        UserReserveInfo = new java.util.HashMap<>();
        UserReserveInfo.put("haveReservation1","1");
        UserReserveInfo.put("haveSignin1","0");
        UserReserveInfo.put("Seat1","1号座位");
        UserReserveInfo.put("location1","博学楼一楼-A101");
        UserReserveInfo.put("time1","2023-5-12 7:00-8:00");

        UserReserveInfo.put("haveReservation2","1");
        UserReserveInfo.put("haveSignin2","0");
        UserReserveInfo.put("Seat2","2号座位");
        UserReserveInfo.put("location2","博学楼一楼-A102");
        UserReserveInfo.put("time2","2023-5-12 20:00-21:00");
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
}
