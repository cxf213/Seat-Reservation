package com.cxfwork.libraryappointment.client;


import java.util.Arrays;
import java.util.List;
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

    public static List<String> getRoomsList(Map<String, String> filter){
        String[] buttonNames = {"A101", "A102", "A103"};
        String[] buttonNames2 = {"A101"};
        if(filter.get("DateID").equals("1")){
            buttonNames = buttonNames2;
        }
        return Arrays.asList(buttonNames);
    }
}
