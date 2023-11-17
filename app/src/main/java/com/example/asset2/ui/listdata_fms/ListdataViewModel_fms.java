package com.example.asset2.ui.listdata_fms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListdataViewModel_fms extends ViewModel {

    private final MutableLiveData<String> mText;

    public ListdataViewModel_fms() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}