package com.example.ghkdw.all_car;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by ghkdw on 2019-10-01.
 */

public class FirstFragment extends Fragment {
    View view;
    Button addDistance;
    Animation anim;
    Context context;
    LinearLayout allDistance, todayDistance, userInfo, percentage;
    TextView viewAllDistance, viewTodayDistance, userInfo_nickname, userInfo_brand, userInfo_car, userInfo_trim, km1, km2, viewPercentage, userInfo_time;
    String code;
    ImageView brandLogo;
    LineChart lineChart;

    public static FirstFragment newInstance(int page, String title, String code) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("SomeTitle", title);
        args.putString("code", code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        code = getArguments().getString("code");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        context = container.getContext();
        addDistance = (Button)view.findViewById(R.id.addDistanceBtn);
        anim = AnimationUtils.loadAnimation(context, R.anim.load_animation);

        allDistance = (LinearLayout)view.findViewById(R.id.allDistance);
        todayDistance = (LinearLayout)view.findViewById(R.id.todayDistance);
        userInfo = (LinearLayout)view.findViewById(R.id.userInfo);
        percentage = (LinearLayout)view.findViewById(R.id.percentage);

        viewAllDistance = (TextView)view.findViewById(R.id.viewAllDistance);
        viewTodayDistance = (TextView)view.findViewById(R.id.viewTodayDistance);
        userInfo_nickname = (TextView)view.findViewById(R.id.userInfo_nickname);
        userInfo_brand = (TextView)view.findViewById(R.id.userInfo_brand);
        userInfo_car = (TextView)view.findViewById(R.id.userInfo_car);
        userInfo_trim = (TextView)view.findViewById(R.id.userInfo_trim);
        viewPercentage = (TextView)view.findViewById(R.id.viewPercentage);
        userInfo_time = (TextView)view.findViewById(R.id.userInfo_time);
        lineChart = (LineChart)view.findViewById(R.id.chart);

        km1 = (TextView)view.findViewById(R.id.km_1);
        km2 = (TextView)view.findViewById(R.id.km_2);


        brandLogo = (ImageView)view.findViewById(R.id.brand_logo);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        allDistance.startAnimation(anim);
        todayDistance.startAnimation(anim);
        userInfo.startAnimation(anim);

        String[] info = null;
        try {
            info = loadInfo();
        } catch(NullPointerException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        for(int i = 0; i < info.length; i++) {
            if(info[i] == null) {
                userInfo_nickname.setText("Guest" + " 님");
                userInfo_brand.setText("제 조 : " + "미등록");
                userInfo_car.setText("차 종 : " + "미등록");
                userInfo_trim.setText("트 림 : " + "미등록");
                userInfo_time.setText("등 록 : " + "미등록");
                break;
            }
            else {

            }
        }
        userInfo_nickname.setText(info[0] + " 님");
        userInfo_brand.setText("제 조 : " + info[1]);
        userInfo_car.setText("차 종 : " + info[2]);
        userInfo_trim.setText("트 림 : " + info[3]);
        userInfo_time.setText("등 록 : " + info[4] + "년 " + info[5] + "월");



        final String year = info[4];
        final String month = info[5];

        if(userInfo_brand.getText().toString().contains("현대")) {
            brandLogo.setImageResource(R.drawable.hyundai);
        }
        else if(userInfo_brand.getText().toString().contains("기아")) {
            brandLogo.setImageResource(R.drawable.kia);
        }
        else if(userInfo_brand.getText().toString().contains("쉐보레")) {
            brandLogo.setImageResource(R.drawable.chevrolet);
        }
        else if(userInfo_brand.getText().toString().contains("쌍용")) {
            brandLogo.setImageResource(R.drawable.ssangyong);
        }
        else if(userInfo_brand.getText().toString().contains("르노")) {
            brandLogo.setImageResource(R.drawable.renault);
        }


        String[] drive = null;
        try {
            drive = loadDrive(code);
        } catch(NullPointerException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        if(drive.length > 1) {
            if(Integer.parseInt(drive[0].trim()) != 0) {
                viewAllDistance.setText(drive[0].trim());
                km1.setVisibility(View.VISIBLE);
                int ref = getPercentage(viewAllDistance.getText().toString(), year, month);
                System.out.println("Output >> " + ref);
                percentage.setVisibility(View.VISIBLE);
                if(ref > 0) {
                    viewPercentage.setText("평균보다 " + ref + "% 많습니다");
                }
                else {
                    viewPercentage.setText("평균보다 " + ref + "% 적습니다");
                }
            }
            viewTodayDistance.setText(drive[1]);
            km2.setVisibility(View.VISIBLE);
        }

        viewAllDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(km1.getVisibility() == View.VISIBLE)
                    return ;
                final View dialogView = (View)View.inflate(context, R.layout.input_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                TextView title = (TextView)dialogView.findViewById(R.id.inputDialog_Title);
                title.setText("주행한 거리를 입력하세요");

                final EditText input = (EditText)dialogView.findViewById(R.id.inputDialog_value);

                Button ok = (Button)dialogView.findViewById(R.id.inputDialog_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewAllDistance.setText(input.getText().toString());
                        if(km1.getVisibility() == View.INVISIBLE) {
                            km1.setVisibility(View.VISIBLE);
                            percentage.setVisibility(View.VISIBLE);

                            int ref = getPercentage(input.getText().toString(), year, month);
                            if(ref > 0) {
                                viewPercentage.setText("평균보다 " + ref + "% 많습니다");
                            }
                            else {
                                viewPercentage.setText("평균보다 " + ref + "% 적습니다");
                            }
                        }
                        dlg.dismiss();
                    }
                });

                Button cancel = (Button)dialogView.findViewById(R.id.inputDialog_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                dlg.show();
            }
        });

        addDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewAllDistance.getText().toString().equals("이 곳을 눌러 입력하세요")) {
                    Toast.makeText(context, "누적주행거리를 먼저 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return ;
                }
                final View dialogView = (View)View.inflate(context, R.layout.input_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                TextView title = (TextView)dialogView.findViewById(R.id.inputDialog_Title);
                title.setText("주행한 거리를 입력하세요");

                final EditText input = (EditText)dialogView.findViewById(R.id.inputDialog_value);

                Button ok = (Button)dialogView.findViewById(R.id.inputDialog_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(viewTodayDistance.getText().toString().equals("금일주행거리")) {
                            viewTodayDistance.setText(input.getText().toString());
                            km2.setVisibility(View.VISIBLE);
                            viewAllDistance.setText(String.valueOf(Integer.parseInt(viewAllDistance.getText().toString()) + Integer.parseInt(input.getText().toString())));
                            dlg.dismiss();
                        }
                        else {
                            int temp = Integer.parseInt(viewTodayDistance.getText().toString()) + Integer.parseInt(input.getText().toString());
                            viewTodayDistance.setText(String.valueOf(temp));
                            viewAllDistance.setText(String.valueOf(Integer.parseInt(viewAllDistance.getText().toString()) + Integer.parseInt(input.getText().toString())));
                            dlg.dismiss();
                        }
                    }
                });

                Button cancel = (Button)dialogView.findViewById(R.id.inputDialog_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                dlg.show();
            }
        });

        lineChart();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(viewAllDistance.getText().toString().equals("이 곳을 눌러 입력하세요"))
            return ;
        try {
            String result = new RegisterActivity().execute(code, viewAllDistance.getText().toString(), viewTodayDistance.getText().toString(), "saveDrive").get();
            Log.i("결과 : ", result);

            String today =  new SimpleDateFormat("yyyyMMdd").format(new Date());
            String reusltR = new RegisterActivity().execute(code, today, "updateRecentDistance").get();
            Log.i("결과 : ", result);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    protected String[] loadInfo() throws NullPointerException {
        String result = null;
        try {
            RegisterActivity task = new RegisterActivity();
            result = task.execute(code, "loadInfo").get();
            Log.i("결과", result);
        } catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(result == null) {
            Toast.makeText(context, "로드 실패", Toast.LENGTH_SHORT).show();
            return null;
        }
        else {
            return result.split("/");
        }
    }

    private int getPercentage(String nowDistance, String year, String month) {
        int nowDis = Integer.parseInt(nowDistance);
        int iYear = Integer.parseInt(year); int iMonth = Integer.parseInt(month);
        String time = new SimpleDateFormat("yyyy").format(System.currentTimeMillis());

        iYear = Integer.parseInt(time) - iYear;
        iMonth = Integer.parseInt(new SimpleDateFormat("MM").format(System.currentTimeMillis())) - iMonth;
        if(iMonth < 0) {
            iMonth = 12 - Math.abs(iMonth);
        }
        int avgDis = ((iYear * 12) + iMonth) * 1700;

        int temp = nowDis - avgDis;
        float resDis = (float)temp / (float)avgDis * 100;
        return (int)Math.abs(resDis);
    }

    private String[] loadDrive(String code) {
        String result = null;
        try {
            RegisterActivity task = new RegisterActivity();
            result = task.execute(code, "loadDrive").get();
            Log.i("결과", result);
        } catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(result == null) {
            Toast.makeText(context, "로드 실패", Toast.LENGTH_SHORT).show();
            return null;
        }
        else {
            return result.split("/");
        }
    }

    private String[] getDistance() {
        String result = null;
        try {
            result = new RegisterActivity().execute(code, "getRecentDistance").get();
            Log.i("결과 : ", result);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result.split("/");
    }

    protected void lineChart() {

        lineChart.invalidate(); //차트 초기화 작업
        lineChart.clear();

        List<Entry> entries = new ArrayList<Entry>();

        String[] distance = getDistance();
        viewTodayDistance.setText(distance[6]);
        entries.add(new Entry(1f, Float.valueOf(viewTodayDistance.getText().toString())));
        entries.add(new Entry(2f, Float.valueOf(distance[0])));
        entries.add(new Entry(3f, Float.valueOf(distance[1])));
        entries.add(new Entry(4f, Float.valueOf(distance[2])));
        entries.add(new Entry(5f, Float.valueOf(distance[3])));
        entries.add(new Entry(6f, Float.valueOf(distance[4])));
        entries.add(new Entry(7f, Float.valueOf(distance[5])));

        LineDataSet lineDataSet = new LineDataSet(entries, "주행거리"); //LineDataSet 선언
        lineDataSet.setColor(Color.BLUE); //LineChart에서 Line Color 설정
        lineDataSet.setCircleColor(Color.BLUE); // LineChart에서 Line Circle Color 설정
        lineDataSet.setCircleColorHole(Color.BLUE);// LineChart에서 Line Hole Circle Color 설정

        LineData lineData = new LineData(); //LineDataSet을 담는 그릇 여러개의 라인 데이터가 들어갈 수 있습니다.
        lineData.addDataSet(lineDataSet);

        lineData.setValueTextColor(Color.BLACK); //라인 데이터의 텍스트 컬러 설정
        lineData.setValueTextSize(9);

        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        //xAxis.setValueFormatter(new ChartXValueFormatter()); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(5, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        xAxis.setTextColor(Color.BLACK); // X축 텍스트컬러설정
        xAxis.setGridColor(Color.BLACK); // X축 줄의 컬러 설정

        YAxis yAxisLeft = lineChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setTextColor(Color.BLACK); //Y축 텍스트 컬러 설정
        yAxisLeft.setGridColor(Color.BLACK); // Y축 줄의 컬러 설정

        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        //y축의 활성화를 제거함

        lineChart.setVisibleXRangeMinimum(60 * 60 * 24 * 1000 * 5); //라인차트에서 최대로 보여질 X축의 데이터 설정
        lineChart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.

        lineChart.setData(lineData);
    }
}