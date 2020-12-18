package com.example.ghkdw.all_car;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomButtons;
    FragmentPagerAdapter adapterViewPager;
    static String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        code = getIntent().getStringExtra("code");

        bottomButtons = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPage);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);


        bottomButtons.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch(item.getItemId()) {
                    case R.id.menu_one:
                        intent = new Intent(MainActivity.this, SettingInfoActivity.class);
                        intent.putExtra("code", code);
                        startActivity(intent);
                        return true;
                    case R.id.menu_two:
                        intent = new Intent(MainActivity.this, TipActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_three:
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        intent.putExtra("code", code);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return FirstFragment.newInstance(0, "Page # 1", code);
                case 1:
                    return SecondFragment.newInstance(1, "Page # 2", code);
                case 2:
                    return ThirdFragment.newInstance(2, "Page # 3", code);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "page" + position;
        }
    }

    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            this.finishAffinity();
        }
    }
}