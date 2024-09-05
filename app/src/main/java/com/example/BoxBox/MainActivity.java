package com.example.BoxBox;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BoxBox.set.MyApplication;
import com.example.BoxBox.set.Setdate;
import com.example.BoxBox.set.Setpath;
import com.example.BoxBox.ui.ParkingFragment;
import com.example.BoxBox.ui.NormalFragment;
import com.example.BoxBox.ui.EventFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.BoxBox.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;

    private static final int PERMISSION_REQUEST_CODE = 1234;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private long backKeyPressedTime = 0;

    int mYear;
    int mMonth;
    int mDay;

    private ImageButton prevDateButton;
    private ImageButton nextDateButton;
    private Setdate setdate;


    ///////////////////layout

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private Toast toast;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    TextView textdate;

    Toolbar toolbar;

    CompoundButton ckFbutton;
    CompoundButton ckRbutton;

    MenuItem ckFitem;
    MenuItem ckRitem;
    ///////////////////setting
    private SharedPreferences pref;

    NormalFragment fragmentN;
    ParkingFragment fragmentE;
    EventFragment fragmentP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkAndRequestPermissions()) {
            initializeApp();
        }
    }

    private void initializeApp() {
        try {
            // 레이아웃 설정
            setContentView(R.layout.activity_main);

            // 저장 경로 설정
            new Setpath();

            // 프래그먼트 초기화
            fragmentN = new NormalFragment();
            fragmentE = new ParkingFragment();
            fragmentP = new EventFragment();

            // 날짜 텍스트뷰 설정
            setdate = new Setdate();
            textdate = (TextView) findViewById(R.id.dateText);
            prevDateButton = findViewById(R.id.prevDateButton);
            nextDateButton = findViewById(R.id.nextDateButton);
            this.todayDate(); //현재시간 받아오기

            // 툴바 설정
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            }

            // SharedPreferences 설정
            pref = MyApplication.getSharedPreferences();

            // 네비게이션 뷰 설정
            setupNavigationViews();

            // 초기 프래그먼트 설정
            if (findViewById(R.id.frameLayout) != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentN).commit();
            }

            // 체크박스 상태 설정
            setupCheckboxes();

            // 메인 액션 설정
            mainAction();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing app: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupNavigationViews() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();
    }

    private void setupCheckboxes() {
        ckFitem = navigationView.getMenu().findItem(R.id.checkFront);
        ckFbutton = (CompoundButton) ckFitem.getActionView();
        ckFbutton.setChecked(pref.getBoolean("ckFbutton", true));

        ckRitem = navigationView.getMenu().findItem(R.id.checkRear);
        ckRbutton = (CompoundButton) ckRitem.getActionView();
        ckRbutton.setChecked(pref.getBoolean("ckRbutton", true));
    }

    public void mainAction(){



        ckFbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveState();
                refrashFrag();
            }
        });

        ckRbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveState();
                refrashFrag();
            }
        });


        textdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "날짜다이얼로그");
                showDate();
            }
        });

        textdate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("TAG", "날짜 초기화");
                todayDate();
                refrashFrag();
                return true;
            }
        });

        prevDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(-1);
            }
        });

        nextDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(1);
            }
        });


        //bottomnavi 눌렸을때
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bt_nav_normal:
                    if(bottomNavigationView.getSelectedItemId() != item.getItemId()){
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentN).commit();
                        Log.d("TAG", "n");
                    }

                    break;
                case R.id.bt_nav_parking:
                    if(bottomNavigationView.getSelectedItemId() != item.getItemId()){
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentE).commit();
                        Log.d("TAG", "p");
                    }
                    break;
                case R.id.bt_nav_event:
                    if(bottomNavigationView.getSelectedItemId() != item.getItemId()){
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentP).commit();
                        Log.d("TAG", "e");
                    }
                    break;
            }
            return true;
        });


        //navi 눌렸을때
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.nav_normal:
                    if(bottomNavigationView.getSelectedItemId() != R.id.bt_nav_normal){
                        bottomNavigationView.setSelectedItemId(R.id.bt_nav_normal);
                    }
                    break;
                case R.id.nav_parking:
                    if(bottomNavigationView.getSelectedItemId() != R.id.bt_nav_parking){
                        bottomNavigationView.setSelectedItemId(R.id.bt_nav_parking);
                    }
                    break;
                case R.id.nav_event:
                    if(bottomNavigationView.getSelectedItemId() != R.id.bt_nav_event){
                        bottomNavigationView.setSelectedItemId(R.id.bt_nav_event);
                    }
                    break;
                case R.id.nav_setting:
                    Intent it = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(it);
                    break;
                case R.id.nav_exit:
                    Log.d("TAG", "종료선택");
                    System.runFinalization();
                    System.exit(0);
                    finish();
                    break;

            }
            return true;
        });
    }

    public void todayDate(){
        mYear = setdate.getYear();
        mMonth = setdate.getMonth();
        mDay = setdate.getDay();

        updateDateDisplay();
    }

    private void updateDateDisplay() {
        textdate.setText(setdate.matchDate(mYear, mMonth, mDay));
    }

    public void showDate(){
        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month + 1;
                mDay = dayOfMonth;
                setdate.setYear(year);
                setdate.setMonth(month + 1);
                setdate.setDay(dayOfMonth);
                updateDateDisplay();
                refrashFrag();
            }
        }, mYear, mMonth - 1, mDay);
        mDatePicker.show();
    }

    private void changeDate(int days) {
        setdate.addDays(days);
        mYear = setdate.getYear();
        mMonth = setdate.getMonth();
        mDay = setdate.getDay();
        updateDateDisplay();
        refrashFrag();
    }

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissionsToRequest = new ArrayList<>();
            for (String permission : REQUIRED_PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                initializeApp();
            } else {
                showPermissionDeniedDialog();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("권한 필요")
                .setMessage("이 앱의 핵심 기능을 사용하려면 저장소 접근 권한이 필요합니다. 설정에서 권한을 허용해주세요.")
                .setPositiveButton("설정", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("종료", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }



/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

 */


    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            /*
            case R.id.action_drawer:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "버튼 클릭됨", Toast.LENGTH_SHORT).show();
                return true;

             */

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void refrashFrag(){

        if(bottomNavigationView.getSelectedItemId() == R.id.bt_nav_normal){
            fragmentN.refreshlist();
        }
        else if(bottomNavigationView.getSelectedItemId() == R.id.bt_nav_parking){
            fragmentE.refreshlist();
        }
        else if(bottomNavigationView.getSelectedItemId() == R.id.bt_nav_event){
            fragmentP.refreshlist();
        }
    }

    private void saveState() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("ckFbutton", ckFbutton.isChecked());
        editor.putBoolean("ckRbutton", ckRbutton.isChecked());
        editor.commit();
        Log.d("TAG", "체크박스 상태 저장완료");
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(getApplicationContext(), " '뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            System.runFinalization();
            System.exit(0);
            finish();
            toast.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
    }
}
