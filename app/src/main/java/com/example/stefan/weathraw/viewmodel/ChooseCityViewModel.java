package com.example.stefan.weathraw.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

public class ChooseCityViewModel extends BaseViewModel {

    private MutableLiveData<Boolean> cancelClick = new MutableLiveData<>();

    public ChooseCityViewModel() {

    }

    public void onCancelClick() {
        cancelClick.setValue(true);
    }

    public LiveData<Boolean> getOnCancelClick() {
        return cancelClick;
    }
}
