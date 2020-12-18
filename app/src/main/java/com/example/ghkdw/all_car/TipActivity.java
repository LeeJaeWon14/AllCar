package com.example.ghkdw.all_car;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class TipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        ExpandableListView listView = (ExpandableListView)findViewById(R.id.expandable_list);

        final ArrayList<HashMap<String, String>> groupData = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> childData = new ArrayList<>();

        HashMap<String, String> groupMileage = new HashMap<>();
        groupMileage.put("group", "연비주행");
        HashMap<String, String> groupExterior = new HashMap<>();
        groupExterior.put("group", "외형관리");
        HashMap<String, String> groupFront = new HashMap<>();
        groupFront.put("group", "전면부");
        HashMap<String, String> groupBottom = new HashMap<>();
        groupBottom.put("group", "하부");
        HashMap<String, String> groupSus = new HashMap<>();
        groupSus.put("group", "서스펜션");

        groupData.add(groupMileage); groupData.add(groupExterior); groupData.add(groupFront);
        groupData.add(groupBottom); groupData.add(groupSus);

        ArrayList<HashMap<String, String>> childListMileage = new ArrayList<>();

        HashMap<String, String> mileage_1 = new HashMap<>();
        mileage_1.put("group", "연비주행");
        mileage_1.put("name", "연비주행 팁");
        childListMileage.add(mileage_1);

        childData.add(childListMileage);

        ArrayList<HashMap<String, String>> childListExterior = new ArrayList<>();

        HashMap<String, String> exterior_1 = new HashMap<>();
        exterior_1.put("group", "외형관리");
        exterior_1.put("name", "작은 흠집은 매니큐어로 숨기기");
        childListExterior.add(exterior_1);

        HashMap<String, String> exterior_2 = new HashMap<>();
        exterior_2.put("group", "외형관리");
        exterior_2.put("name", "주차딱지는 스프레이");
        childListExterior.add(exterior_2);

        HashMap<String, String> exterior_3 = new HashMap<>();
        exterior_3.put("group", "외형관리");
        exterior_3.put("name", "긁힌 부분은 바로 수리");
        childListExterior.add(exterior_3);

        childData.add(childListExterior);

        ArrayList<HashMap<String, String>> childListFront = new ArrayList<>();

        HashMap<String, String> front_1 = new HashMap<>();
        front_1.put("group", "전면부");
        front_1.put("name", "와이퍼에서 뽀드득 소리가 난다면?");
        childListFront.add(front_1);

        childData.add(childListFront);

        ArrayList<HashMap<String, String>> childListBottom = new ArrayList<>();

        HashMap<String, String> bottom_1 = new HashMap<>();
        bottom_1.put("group", "하부");
        bottom_1.put("name", "타이어 교체는 언제?");
        childListBottom.add(bottom_1);

        childData.add(childListBottom);

        ArrayList<HashMap<String, String>> childListSus = new ArrayList<>();

        HashMap<String, String> sus_1 = new HashMap<>();
        sus_1.put("group", "서스펜션");
        sus_1.put("name", "방지턱을 넘을 때 소음이 난다면?");
        childListSus.add(sus_1);

        childData.add(childListSus);

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1, new String[] {"group"},
                new int[] {android.R.id.text1}, childData, android.R.layout.simple_expandable_list_item_2, new String[] {"name", "group"}, new int[] {android.R.id.text1, android.R.id.text2});

        listView.setAdapter(adapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //groupData.get(groupPosition);
                //DB 연동해서 값 가져오기
                String result = null;
                try {
                    RegisterActivity task = new RegisterActivity();
                    result = task.execute(groupData.get(groupPosition).get("group").toString(), String.valueOf(childPosition), "loadTips").get();
                    Log.i("결과 : ", result);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                final View dialogView = (View)View.inflate(TipActivity.this, R.layout.only_ok_or_cancel_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(TipActivity.this);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                ((TextView)dialogView.findViewById(R.id.onlyCancelTitle_2)).setVisibility(View.INVISIBLE);

                TextView msg = (TextView)dialogView.findViewById(R.id.onlyCancelTextView_2);
                msg.setText(result);
                msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                ((Button)dialogView.findViewById(R.id.onlyOk)).setVisibility(View.INVISIBLE);
                ((Button)dialogView.findViewById(R.id.onlyCancel_2)).setVisibility(View.INVISIBLE);

                dlg.show();

                return false;
            }
        });
    }
}