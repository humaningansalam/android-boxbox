package com.example.BoxBox.set;

import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Setpath {

    private String expath, sdpath, Npath, Epath, Ppath, path;

    SharedPreferences pref;

    public Setpath(){
        this.pref = MyApplication.getSharedPreferences();

        this.sdpath = SDCard.getExternalSDCardPath();// sd 절대경로 얻어오기
        this.expath = Environment.getExternalStorageDirectory().getAbsolutePath();// 기본적인 절대경로 얻어오기

        boolean useExternalStorage = pref.getBoolean("useExternalStorage", false);
        String basePath = useExternalStorage && sdpath != null ? sdpath : expath;

        this.Npath = basePath + "/DCIM/BoxBox/NORMAL";
        this.Ppath = basePath + "/DCIM/BoxBox/PARKING";
        this.Epath = basePath + "/DCIM/BoxBox/EVENT";
    }

    public void makePath(String path) {

        File directory = new File(path);

        try {
            if (!directory.exists()) {
                directory.mkdirs();
                Log.d("TAG", "================== 폴더 미존재");
            } else {
                Log.d("TAG", "================== 폴더 이미 존재");
            }

            if (directory.exists()) {
                Log.d("TAG", "================== 폴더 생성완료");

            } else {
                Log.d("TAG", "================== 폴더 생성 실패");

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String Ncheck(){
        path = expath + "/DCIM/BoxBox/NORMAL";
        Log.d("TAG", path);
        return path;
    }

    public String Pcheck(){
        path = expath + "/DCIM/BoxBox/PARKING";
        Log.d("TAG", path);
        return path;
    }

    public String Echeck(){
        path = expath + "/DCIM/BoxBox/EVENT";
        Log.d("TAG", path);
        return path;
    }

    public String getExpath() {
        return expath;
    }

    public String getSdpath() {
        return sdpath;
    }

    public String getNpath() {
        return Npath;
    }

    public String getEpath() {
        return Epath;
    }

    public String getPpath() {
        return Ppath;
    }


}
