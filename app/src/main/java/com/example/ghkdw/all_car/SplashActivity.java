package com.example.ghkdw.all_car;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

//Splash 화면을 보여주는 Activity
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //자동 로그인 인증정보를 저장할 File System
        try {
            FileInputStream inFs = openFileInput("file.txt");
            byte[] txt = new byte[30];
            inFs.read(txt);
            String str = new String(txt);
            inFs.close();

            String[] info = str.split("/");
            String result = null;
            RegisterActivity task = new RegisterActivity();
            result = task.execute(info[0], info[1], "login").get();

            if(result.contains("success")) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("code", info[1]);
                startActivity(intent);
            }
            else if(result == null) {
                showConfirm("인증 내역이 올바르지 않습니다.");
            }
            else {
                showConfirm("인증 내역이 올바르지 않습니다.");
            }
        } catch(IOException e) {
            e.printStackTrace();
            showConfirm("인증 내역이 없습니다.");
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }
    }

    //인증 확인을 물어보는 Dialog 실행 메소드
    private void showConfirm(String msg) {
        final View dialogView = (View)View.inflate(SplashActivity.this, R.layout.only_ok_or_cancel_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        final AlertDialog dlg = builder.create();

        dlg.setView(dialogView);
        dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

        TextView titleView = (TextView)dialogView.findViewById(R.id.onlyCancelTitle_2);
        titleView.setText("로그인");

        TextView textView = (TextView)dialogView.findViewById(R.id.onlyCancelTextView_2);
        textView.setText(msg + "\r\n" + "인증을 받으시겠습니까?");

        Button ok = (Button)dialogView.findViewById(R.id.onlyOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                dlg.dismiss();
            }
        });

        Button cancel = (Button)dialogView.findViewById(R.id.onlyCancel_2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        dlg.show();
    }
}
