package com.cxfwork.libraryappointment.client;


import java.util.ArrayList;
import java.util.Arrays;
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

    public static List<String> getRoomsList(Map<String, String> filter){
        String[] buttonNames = {"A101", "A102", "A103"};
        List<String> lists = new ArrayList<>(Arrays.asList(buttonNames));
        if(filter.get("DateID").equals("1")){
            lists.add("A201");
        }
        return lists;
    }

    public static List<String> getSeatsList(Map<String, String> filter){
        String[] buttonNames = {"1", "2", "3"};
        List<String> lists = new ArrayList<>(Arrays.asList(buttonNames));

        if(Objects.equals(filter.get("Room"), "A102")){
            lists.add("4");
        }
        if(Objects.equals(filter.get("Room"), "A201")){
            lists.add("5");
        }
        return lists;
    }
}
