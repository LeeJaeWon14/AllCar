package com.example.ghkdw.all_car;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import me.relex.circleindicator.CircleIndicator;

public class BottomActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    static String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        code = getIntent().getStringExtra("code");

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPage_bottom);
        adapterViewPager = new BottomActivity.MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator_bottom);
        indicator.setViewPager(viewPager);

        Button repairRecord = (Button)findViewById(R.id.repairBtn_bottom);
        repairRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = (View)View.inflate(BottomActivity.this, R.layout.prev_repair_dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(BottomActivity.this);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                final TextView viewParts = (TextView)dialogView.findViewById(R.id.viewParts);
                final ListView engineList = (ListView)dialogView.findViewById(R.id.listItems_engine_record);
                final ListView frontList = (ListView)dialogView.findViewById(R.id.listItems_front_record);
                final ListView bottomList = (ListView)dialogView.findViewById(R.id.listItems_bottom_record);
                final ListView susList = (ListView)dialogView.findViewById(R.id.listItems_sus_record);

                final RadioGroup rdoGroup = (RadioGroup)dialogView.findViewById(R.id.rdoGroup_record);
                rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(rdoGroup.getCheckedRadioButtonId()) {
                            case R.id.rdoEngine:
                                viewParts.setVisibility(View.INVISIBLE);
                                frontList.setVisibility(View.INVISIBLE);
                                bottomList.setVisibility(View.INVISIBLE);
                                susList.setVisibility(View.INVISIBLE);
                                final String[] engineItems = getResources().getStringArray(R.array.engine);
                                ArrayAdapter<String> engineAdapter = new ArrayAdapter<String>(BottomActivity.this, android.R.layout.simple_list_item_1, engineItems);
                                engineList.setAdapter(engineAdapter);
                                engineList.setVisibility(View.VISIBLE);
                                engineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        engineList.setVisibility(View.INVISIBLE);
                                        viewParts.setVisibility(View.VISIBLE);
                                        viewParts.setText(engineItems[position]);
                                    }
                                });
                                break;
                            case R.id.rdoFront:
                                viewParts.setVisibility(View.INVISIBLE);
                                engineList.setVisibility(View.INVISIBLE);
                                bottomList.setVisibility(View.INVISIBLE);
                                susList.setVisibility(View.INVISIBLE);
                                final String[] frontItems = getResources().getStringArray(R.array.front);
                                ArrayAdapter<String> frontAdapter = new ArrayAdapter<String>(BottomActivity.this, android.R.layout.simple_list_item_1, frontItems);
                                frontList.setAdapter(frontAdapter);
                                frontList.setVisibility(View.VISIBLE);
                                frontList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        frontList.setVisibility(View.INVISIBLE);
                                        viewParts.setVisibility(View.VISIBLE);
                                        viewParts.setText(frontItems[position]);
                                    }
                                });
                                break;
                            case R.id.rdoBottom:
                                viewParts.setVisibility(View.INVISIBLE);
                                engineList.setVisibility(View.INVISIBLE);
                                frontList.setVisibility(View.INVISIBLE);
                                susList.setVisibility(View.INVISIBLE);
                                final String[] bottomItems = getResources().getStringArray(R.array.bottom);
                                ArrayAdapter<String> bottomAdapter = new ArrayAdapter<String>(BottomActivity.this, android.R.layout.simple_list_item_1, bottomItems);
                                bottomList.setAdapter(bottomAdapter);
                                bottomList.setVisibility(View.VISIBLE);
                                bottomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        bottomList.setVisibility(View.INVISIBLE);
                                        viewParts.setVisibility(View.VISIBLE);
                                        viewParts.setText(bottomItems[position]);
                                    }
                                });
                                break;
                            case R.id.rdoSus:
                                viewParts.setVisibility(View.INVISIBLE);
                                engineList.setVisibility(View.INVISIBLE);
                                frontList.setVisibility(View.INVISIBLE);
                                bottomList.setVisibility(View.INVISIBLE);
                                final String[] susItems = getResources().getStringArray(R.array.suspension);
                                ArrayAdapter<String> susAdapter = new ArrayAdapter<String>(BottomActivity.this, android.R.layout.simple_list_item_1, susItems);
                                susList.setAdapter(susAdapter);
                                susList.setVisibility(View.VISIBLE);
                                susList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        susList.setVisibility(View.INVISIBLE);
                                        viewParts.setVisibility(View.VISIBLE);
                                        viewParts.setText(susItems[position]);
                                    }
                                });
                                break;
                            default:
                                Toast.makeText(BottomActivity.this, "개발중", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

                final TextView unit = (TextView)dialogView.findViewById(R.id.record_textView);
                final CheckBox chkBox = (CheckBox)dialogView.findViewById(R.id.chkBox);
                chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) unit.setText("개월");
                        else unit.setText("km");
                    }
                });

                final EditText inputValue = (EditText)dialogView.findViewById(R.id.inputRecord);
                Button ok = (Button)dialogView.findViewById(R.id.record_button);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(viewParts.getVisibility() == View.INVISIBLE) {
                            Toast.makeText(BottomActivity.this, "부품을 먼저 선택해주세요!", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        String s_inputValue = inputValue.getText().toString();
                        if(chkBox.isChecked()) {
                            int avgDistance = Integer.parseInt(s_inputValue);
                            s_inputValue = String.valueOf(avgDistance * 1600);
                        }
                        if(inputValue.getText().toString().equals("")) {
                            Toast.makeText(BottomActivity.this, "거리나 시간을 입력해주세요!", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if(saveRecord(code, viewParts.getText().toString(), s_inputValue))
                            Toast.makeText(BottomActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(BottomActivity.this, "저장이 실패하였습니다", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();
                    }
                });


                dlg.show();
            }
        });

        showLoad();
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BottomFragment.newInstance(0, "Page # 1", code);
                case 1:
                    return SuspensionFragment.newInstance(1, "Page # 2", code);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "page" + position;
        }
    }

    public void showLoad() {
        final View dialogView = (View)View.inflate(BottomActivity.this, R.layout.only_ok_or_cancel_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(BottomActivity.this);
        final AlertDialog dlg = builder.create();

        dlg.setView(dialogView);
        dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

        TextView title = (TextView)dialogView.findViewById(R.id.onlyCancelTitle_2);
        title.setText("알림");

        TextView msg = (TextView)dialogView.findViewById(R.id.onlyCancelTextView_2);
        msg.setText("이전에 정비했던 사항이 있다면 \r\n 기록해주세요!");

        Button ok = (Button)dialogView.findViewById(R.id.onlyOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        Button cancel = (Button)dialogView.findViewById(R.id.onlyCancel_2);
        cancel.setVisibility(View.INVISIBLE);

        dlg.show();
    }

    private boolean saveRecord(String code, String part, String distance) {
        //part - 소모품, distance - 교체한 적산거리

        try {
            RegisterActivity task = new RegisterActivity();
            String result = task.execute(code, part, distance, "saveRecord").get();
            Log.i("결과 : ", result);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}