package com.cxfwork.libraryappointment.ui.quickreservation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuickReservationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public QuickReservationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}