package com.example.ghkdw.all_car;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    Button requestCode;
    EditText inputEmail, inputCode, inputDomain;
    LinearLayout codeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestCode = (Button)findViewById(R.id.requestCode);
        inputEmail = (EditText)findViewById(R.id.inputEmail);
        inputCode = (EditText)findViewById(R.id.inputCode);
        codeLayout = (LinearLayout)findViewById(R.id.secretCodeLayout);
        inputDomain = (EditText)findViewById(R.id.inputDomain);

        inputDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] domainItems = getResources().getStringArray(R.array.spinnerArray);
                final View dialogView = (View)View.inflate(LoginActivity.this, R.layout.list_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                TextView titleView = (TextView)dialogView.findViewById(R.id.listTitle);
                titleView.setText("도메인 선택");

                final EditText directDomain = (EditText)dialogView.findViewById(R.id.directDomain);
                final ListView listView = (ListView)dialogView.findViewById(R.id.listItems);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, domainItems);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(domainItems[position].equals("직접 입력")) {
                            listView.setVisibility(View.INVISIBLE);
                            directDomain.setVisibility(View.VISIBLE);
                            Button cancel = (Button)dialogView.findViewById(R.id.listCancel);
                            cancel.setText("확인");
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    inputDomain.setText(directDomain.getText().toString());
                                    dlg.dismiss();
                                }
                            });
                        }
                        else {
                            inputDomain.setText(domainItems[position]);
                            dlg.dismiss();
                        }
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
        });

        final String code = String.valueOf(codeMake());
        requestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result;
                try {
                    RegisterActivity task = new RegisterActivity();
                    result = task.execute(inputEmail.getText().toString(), inputDomain.getText().toString(), code, "accredit").get();
                    if(result.contains("accredit success")) {
                        Toast.makeText(LoginActivity.this, "전송되었습니다.", Toast.LENGTH_SHORT).show();
                        requestCode.setVisibility(View.INVISIBLE);
                        inputEmail.setEnabled(false);
                        codeLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("결과", result);
                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = (View)View.inflate(LoginActivity.this, R.layout.only_ok_or_cancel_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                TextView titleView = (TextView)dialogView.findViewById(R.id.onlyCancelTitle_2);
                titleView.setText("인증 확인");

                TextView textView = (TextView)dialogView.findViewById(R.id.onlyCancelTextView_2);
                if(inputCode.getText().toString().equals(code))
                    textView.setText("인증이 성공하였습니다!");
                else
                    textView.setText("인증이 실패하였습니다!");

                Button ok = (Button)dialogView.findViewById(R.id.onlyOk);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(inputCode.getText().toString().equals(code)) {
                            try {
                                FileOutputStream outFs = openFileOutput("file.txt", Context.MODE_PRIVATE);
                                String str = inputEmail.getText().toString() + "/" + inputCode.getText().toString();
                                outFs.write(str.getBytes());
                                outFs.close();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                            String result;
                            try {
                                RegisterActivity task = new RegisterActivity();
                                result = task.execute(inputEmail.getText().toString() + "@" + inputDomain.getText().toString(), "car", "fuel", "nickname", code, "join").get();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                            final View dialogView = (View)View.inflate(LoginActivity.this, R.layout.only_ok_or_cancel_dialog, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            final AlertDialog dlg = builder.create();

                            dlg.setView(dialogView);
                            dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                            TextView titleView = (TextView)dialogView.findViewById(R.id.onlyCancelTitle_2);
                            titleView.setText("등록");

                            TextView msg = (TextView)dialogView.findViewById(R.id.onlyCancelTextView_2);
                            msg.setText("차량을 등록해주세요!");

                            Button ok = (Button)dialogView.findViewById(R.id.onlyOk);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(LoginActivity.this, SettingInfoActivity.class);
                                    intent.putExtra("code", code);
                                    startActivity(intent);
                                    dlg.dismiss();
                                }
                            });

                            ((Button)dialogView.findViewById(R.id.onlyCancel_2)).setVisibility(View.INVISIBLE);

                            dlg.show();
                        }
                        else {
                            dlg.dismiss();
                        }
                    }
                });

                Button cancel = (Button)dialogView.findViewById(R.id.onlyCancel_2); cancel.setVisibility(View.INVISIBLE);

                dlg.show();
            }
        });
    }

    private int codeMake() {
        int rnd = 0;
        while(true) {
            rnd = new Random().nextInt(99999);
            if(rnd > 10000)
                break;
        }

        return rnd;
    }
}
