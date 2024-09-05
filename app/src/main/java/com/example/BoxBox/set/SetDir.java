package com.example.BoxBox.set;

import android.util.Log;

import java.io.File;

public class SetDir {
    String expath, path;
    public SetDir(){
        expath = new Setpath().getExpath();
    }


    public File makeDir(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                Log.d("TAG", "폴더 생성 완료: " + path);
            } else {
                Log.e("TAG", "폴더 생성 실패: " + path);
                return null;
            }
        }
        return directory;
    }

    public static boolean fileDelete(String filePath){
        //filePath : 파일경로 및 파일명이 포함된 경로입니다.
        try {
            File file = new File(filePath);

            // 파일이 존재 하는지 체크
            if(file.exists()) {
                file.delete();
                Log.d("TAG", "삭제완료");
                return true;  // 파일 삭제 성공여부를 리턴값으로 반환해줄 수 도 있습니다.
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
