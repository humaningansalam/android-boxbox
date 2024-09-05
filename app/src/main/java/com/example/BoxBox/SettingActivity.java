package com.example.BoxBox;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;


import com.example.BoxBox.setting.Setting_InfoFragment;
import com.example.BoxBox.setting.Setting_StorageFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class SettingActivity extends AppCompatActivity {

    private ViewPager2 mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        TabLayout mTabLayout = findViewById(R.id.tabLayout);

        mViewpager = findViewById(R.id.viewPager);
        mViewpager.setOffscreenPageLimit(2);


        ViewPagerFragmentAdapter mFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        mFragmentAdapter.addFragment(new Setting_InfoFragment());
        mFragmentAdapter.addFragment(new Setting_StorageFragment());
        mViewpager.setAdapter(mFragmentAdapter);

        new TabLayoutMediator(mTabLayout, mViewpager, (tab, position) -> {
            switch (position) {
                case 0 :
                    tab.setText("정보");
                    break;
                case 1 :
                    tab.setText("저장위치 설정");
                    break;
                default:
                    Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }).attach();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting2home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
