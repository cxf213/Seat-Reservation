package com.cxfwork.libraryappointment.client;


import java.util.Map;

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
}
