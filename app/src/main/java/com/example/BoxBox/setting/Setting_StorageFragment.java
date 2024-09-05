package com.example.BoxBox.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.BoxBox.R;
import com.example.BoxBox.set.MyApplication;
import com.example.BoxBox.set.SDCard;

public class Setting_StorageFragment extends Fragment {

    private Activity activity;
    private RadioGroup radioGroupStorage;
    private RadioButton internalStorageRadio;
    private RadioButton externalStorageRadio;
    private TextView storedPathText;
    private SharedPreferences pref;


    public Setting_StorageFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            activity = getActivity();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting_storage, container, false);

        pref = MyApplication.getSharedPreferences();

        radioGroupStorage = root.findViewById(R.id.radioGroupStorage);
        internalStorageRadio = root.findViewById(R.id.internal_storage);
        externalStorageRadio = root.findViewById(R.id.external_storage);
        storedPathText = root.findViewById(R.id.storedPathText);

        // SD 카드가 없으면 외장 메모리 옵션을 비활성화
        if (SDCard.getExternalSDCardPath() == null) {
            externalStorageRadio.setEnabled(false);
        }

        // 저장된 설정 불러오기
        boolean useExternalStorage = pref.getBoolean("useExternalStorage", false);
        if (useExternalStorage && externalStorageRadio.isEnabled()) {
            externalStorageRadio.setChecked(true);
            storedPathText.setText("(현재저장경로 : 외장 메모리)");
        } else {
            internalStorageRadio.setChecked(true);
            storedPathText.setText("(현재저장경로 : 내장 메모리)");
        }

        radioGroupStorage.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = pref.edit();
            if (checkedId == R.id.external_storage) {
                editor.putBoolean("useExternalStorage", true);
                storedPathText.setText("(현재저장경로 : 외장 메모리)");
                Toast.makeText(activity, "외장 메모리로 저장 위치가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                editor.putBoolean("useExternalStorage", false);
                storedPathText.setText("(현재저장경로 : 내장 메모리)");
                Toast.makeText(activity, "내장 메모리로 저장 위치가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        });

        return root;
    }

    /*
    private void showPermissionDialog() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(activity, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(activity, "권한이 거부되었습니다. " + deniedPermissions.get(0), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("외부 저장소에 접근하기 위해 권한 설정이 필요합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
     */


}
