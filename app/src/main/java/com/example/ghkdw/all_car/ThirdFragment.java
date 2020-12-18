package com.example.ghkdw.all_car;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ghkdw on 2019-10-01.
 */

public class ThirdFragment extends Fragment {
    View view;
    String code;
    Context context;
    LinearLayout refuelRecordLayout, refuelPriceLayout;
    TextView refuelRecord, refuelPrice, moreRecord, priceDate;
    Animation anim;

    public static ThirdFragment newInstance(int page, String title, String code) {
        ThirdFragment fragment = new ThirdFragment();
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
        view = inflater.inflate(R.layout.fragment_third, container, false);
        context = container.getContext();
        anim = AnimationUtils.loadAnimation(context, R.anim.load_animation);

        refuelRecordLayout = (LinearLayout)view.findViewById(R.id.recent_refuel_layout);
        refuelPriceLayout = (LinearLayout)view.findViewById(R.id.price_layout);

        refuelRecord = (TextView)view.findViewById(R.id.recent_refuel);
        refuelPrice = (TextView)view.findViewById(R.id.refuel_price);
        moreRecord = (TextView)view.findViewById(R.id.more_refuel);
        priceDate = (TextView)view.findViewById(R.id.priceDate);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        refuelRecordLayout.startAnimation(anim);
        refuelPriceLayout.startAnimation(anim);

        String[] record = null;
        try {
            record = loadLastRecord();
            refuelRecord.setText("최근 주유 : " + "\r\n" + record[0].trim() + "에 " + "\r\n" + record[1] + " 주유소에서 " + "\r\n" + record[2].trim() + "원만큼 주유하셨습니다");
        } catch(NullPointerException e) {
            refuelRecord.setText("최근 주유 : 없음");
        }

        //if(record[0] == null || record[0].equals("")) );


        moreRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RefuelActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        });



        priceDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 기준");
        new XmlParseTask(refuelPrice).execute("http://www.opinet.co.kr/api/avgAllPrice.do?out=xml&code=F624191007");
    }

    private String[] loadLastRecord() throws NullPointerException {
        String result = null;
        try {
            RegisterActivity task = new RegisterActivity();
            result = task.execute(code, "loadLastRefuelRecord").get();
            Log.i("결과 : ", result);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(result == null) {
            Toast.makeText(context, "로드 실패", Toast.LENGTH_SHORT).show();
            return null;
        }
        else if(result.contains("No value")) {
            return null;
        }
        else {
            return result.split("/");
        }
    }
}