package com.example.asset2.ui.listdata_net;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListdataViewModel_net extends ViewModel {

    private final MutableLiveData<String> mText;

    public ListdataViewModel_net() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}