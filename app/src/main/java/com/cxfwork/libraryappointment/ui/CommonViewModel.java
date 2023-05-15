package com.cxfwork.libraryappointment.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class CommonViewModel extends ViewModel {
    private MutableLiveData<Map<String, String>> UserReservation = new MutableLiveData<>();
    private MutableLiveData<Map<String, String>> NewReservation = new MutableLiveData<>();

    public CommonViewModel() {
        // 在构造函数中对数据进行初始化
        Map<String, String> initialData1 = new HashMap<>();
        initialData1.put("haveReservation", "0");
        UserReservation.setValue(initialData1);
        NewReservation.setValue(initialData1);
    }

    public LiveData<Map<String, String>> getUserReservation() {
        return UserReservation;
    }

    public LiveData<Map<String, String>> getNewReservation() { return NewReservation; }

    public void setUserReservation(Map<String, String> newData) {
        UserReservation.setValue(newData);
    }

    public void setNewReservation(Map<String, String> newData) {
        NewReservation.setValue(newData);
    }
}
