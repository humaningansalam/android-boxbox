package com.example.BoxBox.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NobViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NobViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("영상을 찾을수 없습니다 파일형식 : 20210603_12h12m30s_F.mp4");
    }

    public LiveData<String> getText() {
        return mText;
    }
}