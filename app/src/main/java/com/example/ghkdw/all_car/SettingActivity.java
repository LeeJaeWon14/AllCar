package com.example.ghkdw.all_car;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SettingActivity extends AppCompatActivity {
    Button setNickname, setLogout, appInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final String code = getIntent().getStringExtra("code");

        setNickname = (Button)findViewById(R.id.setNickname);
        setLogout = (Button)findViewById(R.id.setLogout);
        appInfo = (Button)findViewById(R.id.viewAppInfo);


        setNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = (View)View.inflate(SettingActivity.this, R.layout.input_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                final AlertDialog firstDlg = builder.create();

                firstDlg.setView(dialogView);
                firstDlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                TextView title = (TextView)dialogView.findViewById(R.id.inputDialog_Title);
                title.setText("닉네임을 변경하세요");

                final EditText input = (EditText)dialogView.findViewById(R.id.inputDialog_value);

                Button ok = (Button)dialogView.findViewById(R.id.inputDialog_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View dialogView = (View)View.inflate(SettingActivity.this, R.layout.only_ok_or_cancel_dialog, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                        final AlertDialog dlg = builder.create();

                        dlg.setView(dialogView);
                        dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                        TextView title = (TextView)dialogView.findViewById(R.id.onlyCancelTitle_2);
                        title.setVisibility(View.INVISIBLE);

                        TextView msg = (TextView)dialogView.findViewById(R.id.onlyCancelTextView_2);
                        msg.setText("변경하시겠습니까?");

                        Button ok = (Button)dialogView.findViewById(R.id.onlyOk);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String result = null;
                                try {
                                    RegisterActivity task = new RegisterActivity();
                                    result = task.execute(code, input.getText().toString(), "nickname").get();
                                    Log.i("결과", result);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                if(result.contains("success")) {
                                    Toast.makeText(SettingActivity.this, "변경되었습니다.", Toast.LENGTH_SHORT).show();
                                    dlg.dismiss();
                                    firstDlg.dismiss();
                                }
                                else {
                                    Toast.makeText(SettingActivity.this, "변경에 실패하였습니다..", Toast.LENGTH_SHORT).show();
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

                Button cancel = (Button)dialogView.findViewById(R.id.inputDialog_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firstDlg.dismiss();
                    }
                });

                firstDlg.show();
            }
        });

        appInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AppInfoActivity.class);
                startActivity(intent);
            }
        });

        setLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = (View)View.inflate(SettingActivity.this, R.layout.only_ok_or_cancel_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                ((TextView)dialogView.findViewById(R.id.onlyCancelTitle_2)).setVisibility(View.INVISIBLE);
                ((TextView)dialogView.findViewById(R.id.onlyCancelTextView_2)).setText("정말 탈퇴하시겠습니까?" + "\r\n" + "이 작업은 취소될 수 없습니다.");

                ((Button)dialogView.findViewById(R.id.onlyCancel_2)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                ((Button)dialogView.findViewById(R.id.onlyOk)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String result = null;
                        try {
                            result = new RegisterActivity().execute(code, "drop").get();
                            Log.i("결과 : ", result);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        if(result.contains("success")) {
                            final View dialogView = (View)View.inflate(SettingActivity.this, R.layout.only_ok_or_cancel_dialog, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                            final AlertDialog dlg = builder.create();

                            dlg.setView(dialogView);
                            dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                            String sysDir = Environment.getRootDirectory().getAbsolutePath();
                            File[] sysFiles = (new File(sysDir).listFiles());

                            String strFrame = null;
                            for(int i = 0; i < sysFiles.length; i++) {
                                if (sysFiles[i].isDirectory() == true)
                                    strFrame = "<폴더>" + sysFiles[i].toString();
                                else {
                                    strFrame = "<파일>" + sysFiles[i].toString();
                                }
                            }
                            System.out.println(strFrame);

                            ((TextView)dialogView.findViewById(R.id.onlyCancelTitle_2)).setVisibility(View.INVISIBLE);
                            ((TextView)dialogView.findViewById(R.id.onlyCancelTextView_2)).setText("탈퇴가 완료되었습니다." + "\r\n" + "그동안 사용해주셔어 감사합니다.");

                            ((Button)dialogView.findViewById(R.id.onlyOk)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.finishAffinity(SettingActivity.this);
                                    Intent intent = new Intent(SettingActivity.this, SplashActivity.class);
                                    startActivity(intent);
                                }
                            });

                            ((Button)dialogView.findViewById(R.id.onlyCancel_2)).setVisibility(View.INVISIBLE);

                            dlg.show();
                        }
                        else {
                            final View dialogView = (View)View.inflate(SettingActivity.this, R.layout.only_ok_or_cancel_dialog, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                            final AlertDialog dlg = builder.create();

                            dlg.setView(dialogView);
                            dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                            ((TextView)dialogView.findViewById(R.id.onlyCancelTitle_2)).setVisibility(View.INVISIBLE);
                            ((TextView)dialogView.findViewById(R.id.onlyCancelTextView_2)).setText("탈퇴가 실패하였습니다.");

                            ((Button)dialogView.findViewById(R.id.onlyOk)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dlg.dismiss();
                                }
                            });

                            ((Button)dialogView.findViewById(R.id.onlyCancel_2)).setVisibility(View.INVISIBLE);

                            dlg.show();
                        }
                    }
                });

                dlg.show();
            }
        });
    }
}
