package com.cxfwork.libraryappointment.client;


import java.util.Map;

public class ReserveService {
    private Map<String,String> reserveInfo1;
    private Map<String,String> reserveInfo2;
    public ReserveService(){
        reserveInfo1 = new java.util.HashMap<>();
        reserveInfo2 = new java.util.HashMap<>();
        reserveInfo1.put("haveReservation","1");
        reserveInfo1.put("haveSignin","0");
        reserveInfo1.put("time","0");
        reserveInfo1.put("Seat","1号座位");
        reserveInfo1.put("location","博学楼一楼-A101");
        reserveInfo1.put("time","2023-5-12 20:00-21:00");
        reserveInfo2.put("haveReservation","1");
        reserveInfo2.put("haveSignin","0");
        reserveInfo2.put("time","0");
        reserveInfo2.put("Seat","2号座位");
        reserveInfo2.put("location","博学楼一楼-A102");
        reserveInfo2.put("time","2023-5-12 20:00-21:00");
    }
    public Map<String,String> getReserveInfo1(){

        return reserveInfo1;
    }
    public Map<String,String> getReserveInfo2(){

        return reserveInfo2;
    }
    public void signin1(){
        reserveInfo1.replace("haveSignin","1");
    }
    public void signin2(){
        reserveInfo2.replace("haveSignin","1");
    }
    public void cancel1(){
        reserveInfo1.replace("haveReservation","0");
    }
    public void cancel2(){
        reserveInfo2.replace("haveReservation","0");
    }
}
