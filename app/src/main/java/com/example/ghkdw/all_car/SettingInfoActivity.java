package com.example.ghkdw.all_car;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingInfoActivity extends AppCompatActivity {
    EditText inputCar, inputBrand, inputTrim, inputYear, inputMonth;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);

        String code_t = getIntent().getStringExtra("code");
        final String code = code_t.replaceAll(" ", "");

        inputCar = (EditText)findViewById(R.id.inputCar);
        inputBrand = (EditText)findViewById(R.id.inputBrand);
        inputTrim = (EditText)findViewById(R.id.inputTrim);
        inputYear = (EditText)findViewById(R.id.inputYear);
        inputMonth = (EditText)findViewById(R.id.inputMonth);
        nextBtn = (Button)findViewById(R.id.nextBtn);

        inputBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeListDialog(getResources().getStringArray(R.array.brand), "Brand", inputBrand);
            }
        });

        inputTrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputCar.getText().toString().equals("")) {
                    Toast.makeText(SettingInfoActivity.this, "차종을 먼저 선택해주세요!", Toast.LENGTH_SHORT).show();
                    return ;
                }
                makeListDialog(getResources().getStringArray(R.array.trim), "Trim", inputTrim);
            }
        });

        inputCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = null;
                switch(inputBrand.getText().toString()) {
                    case "":
                        Toast.makeText(SettingInfoActivity.this, "제조사를 먼저 선택해주세요!", Toast.LENGTH_SHORT).show();
                        return ;
                    case "현대":
                        items = getResources().getStringArray(R.array.hyundai);
                        break;
                    case "기아":
                        items = getResources().getStringArray(R.array.kia);
                        break;
                    case "쉐보레":
                        items = getResources().getStringArray(R.array.chevorlet);
                        break;
                    case "르노":
                        items = getResources().getStringArray(R.array.renault);
                        break;
                    case "쌍용":
                        items = getResources().getStringArray(R.array.ssangyong);
                        break;
                    default:
                        break;
                }

                makeListDialog(items, "Car", inputCar);
            }
        });

        inputYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeInputDialog(inputYear);
            }
        });

        inputMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeInputDialog(inputMonth);
            }
        });



        nextBtn.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {
                final View dialogView = (View)View.inflate(SettingInfoActivity.this, R.layout.only_ok_or_cancel_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingInfoActivity.this);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                TextView title = (TextView)dialogView.findViewById(R.id.onlyCancelTitle_2);
                title.setText("확인");

                TextView msg = (TextView)dialogView.findViewById(R.id.onlyCancelTextView_2);
                msg.setText("등록하시겠습니까?");

                Button ok = (Button)dialogView.findViewById(R.id.onlyOk);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            RegisterActivity task = new RegisterActivity();
                            result = task.execute(code, inputBrand.getText().toString(), inputCar.getText().toString(), inputTrim.getText().toString(),
                                    inputYear.getText().toString(), inputMonth.getText().toString(), "setInfo").get();
                            Log.i("결과", result);
                        } catch(Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SettingInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if(result.contains("success")) {
                            Intent intent = new Intent(SettingInfoActivity.this, MainActivity.class);
                            intent.putExtra("code", code);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(SettingInfoActivity.this, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        }
                    }
                });

                Button cancel = (Button)dialogView.findViewById(R.id.onlyCancel_2);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                dlg.show();
            }
        });
    }

    protected void makeListDialog(final String[] items, String group, final EditText myText) {
        final View dialogView = (View)View.inflate(SettingInfoActivity.this, R.layout.list_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingInfoActivity.this);
        final AlertDialog dlg = builder.create();

        dlg.setView(dialogView);
        dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

        TextView title = (TextView)dialogView.findViewById(R.id.listTitle);
        if(group.equals("Brand")) title.setText("제조사를 선택하세요");
        if(group.equals("Car")) title.setText("차량을 선택하세요");
        if(group.equals("Trim")) title.setText("트림을 선택하세요");

        ListView listView = (ListView)dialogView.findViewById(R.id.listItems);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myText.setText(items[position]);
                dlg.dismiss();
            }
        });

        Button cancel = (Button)dialogView.findViewById(R.id.listCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.show();
    }

    protected void makeInputDialog(final EditText input) {
        final View dialogView = (View)View.inflate(SettingInfoActivity.this, R.layout.input_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingInfoActivity.this);
        final AlertDialog dlg = builder.create();

        dlg.setView(dialogView);
        dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

        TextView title = (TextView)dialogView.findViewById(R.id.inputDialog_Title);
        title.setText("입력하세요");

        final EditText inputVal = (EditText)dialogView.findViewById(R.id.inputDialog_value);

        Button ok = (Button)dialogView.findViewById(R.id.inputDialog_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText(inputVal.getText().toString());
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
}