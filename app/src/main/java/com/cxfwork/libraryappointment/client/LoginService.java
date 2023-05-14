package com.cxfwork.libraryappointment.client;

public class LoginService {
    public static String login(String username, String password) {
        if(username.equals("") && password.equals("")) {
            return "Success";
        } else if (username.equals("1")) {
            return "Register";
        }
        return "Failed";
    }
    public static void register(String username, String password,String stuname,String stuclass,String stuphone) {

    }
}
