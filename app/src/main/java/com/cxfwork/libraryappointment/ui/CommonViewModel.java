package com.cxfwork.libraryappointment.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class CommonViewModel extends ViewModel {
    private MutableLiveData<Map<String, String>> UserReservation1 = new MutableLiveData<>();
    private MutableLiveData<Map<String, String>> UserReservation2 = new MutableLiveData<>();
    public CommonViewModel() {
        // 在构造函数中对数据进行初始化
        Map<String, String> initialData1 = new HashMap<>();
        initialData1.put("haveReservation", "0");
        UserReservation1.setValue(initialData1);

        Map<String, String> initialData2 = new HashMap<>();
        initialData2.put("haveReservation", "0");
        UserReservation2.setValue(initialData2);
    }
    public LiveData<Map<String, String>> getUserReservation1() {
        return UserReservation1;
    }
    public LiveData<Map<String, String>> getUserReservation2() {
        return UserReservation2;
    }

    public void setUserReservation1(Map<String, String> newData) {
        UserReservation1.setValue(newData);
    }
    public void setUserReservation2(Map<String, String> newData) {
        UserReservation2.setValue(newData);
    }
}
