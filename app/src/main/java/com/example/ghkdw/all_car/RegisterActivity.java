package com.example.ghkdw.all_car;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ghkdw on 2019-09-26.
 */

public class RegisterActivity extends AsyncTask<String, Void, String> {
    public static String ip = "172.30.1.50:8085";
    String sendMsg, receiveMsg;
    String serviceIp = "http://" + ip + "/All_Car/All_Car_Connect.jsp";

    public static String temp;
    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;

            URL url = new URL("http://" + ip + "/All_Car/All_Car_Connect.jsp");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "utf-8");

            String type;
            for (int i = 0; i < strings.length; i++) {
                if (strings[i].equals("join")) {
                    sendMsg = "email=" + strings[0] + "&car=" + strings[1] + "&fuel=" + strings[2] +
                            "&nickname=" + strings[3] + "&secret_code=" + strings[4] + "&type=" + strings[5];
                }
                if (strings[i].equals("login")) {
                    sendMsg = "email=" + strings[0] + "&secret_code=" + strings[1] + "&type=" + strings[2];
                }
                if (strings[i].equals("accredit")) {
                    sendMsg = "email=" + strings[0] + "&domain=" + strings[1] + "&secret_code=" + strings[2] + "&type=" + strings[3];
                }
                if (strings[i].equals("setInfo")) {
                    sendMsg = "code=" + strings[0] + "&brand=" + strings[1] + "&car=" + strings[2] + "&trim=" + strings[3] + "&year=" + strings[4]
                        + "&month=" + strings[5] + "&type=" + strings[6];
                }
                if (strings[i].equals("nickname")) {
                    sendMsg = "code=" + strings[0] + "&nickname=" + strings[1] + "&type=" + strings[2];
                }
                if (strings[i].equals("loadInfo") || strings[i].equals("loadDrive")) {
                    sendMsg = "code=" + strings[0] + "&type=" + strings[1];
                }
                if (strings[i].equals("saveRecord")) {
                    sendMsg = "code=" + strings[0] + "&part=" + strings[1] + "&distance=" + strings[2] + "&type=" + strings[3];
                }
                if (strings[i].equals("loadRecord")) {
                    sendMsg = "code=" + strings[0] + "&part=" + strings[1] + "&type=" + strings[2];
                }
                if (strings[i].equals("loadTips")) {
                    sendMsg = "category=" + strings[0] + "&index=" + strings[1] + "&type=" + strings[2];
                }
                if (strings[i].equals("loadLastRefuelRecord")) {
                    sendMsg = "code=" + strings[0] + "&type=" + strings[1];
                }
                if (strings[i].equals("loadRefuelRecordDate")) {
                    sendMsg = "code=" + strings[0] + "&type=" + strings[1];
                }
                if (strings[i].equals("loadRefuelRecordLocation")) {
                    sendMsg = "code=" + strings[0] + "&type=" + strings[1];
                }
                if (strings[i].equals("loadRefuelRecordPrice")) {
                    sendMsg = "code=" + strings[0] + "&type=" + strings[1];
                }
                if (strings[i].equals("saveDrive")) {
                    sendMsg = "code=" + strings[0] + "&allDistance=" + strings[1] + "&todayDistance=" + strings[2] + "&type=" + strings[3];
                }
                if (strings[i].equals("saveRefuelRecord") || strings[i].equals("deleteRefuelRecord")) {
                    sendMsg = "code=" + strings[0] + "&date=" + strings[1] + "&location=" + strings[2] + "&price=" + strings[3] + "&type=" + strings[4];
                }
                if (strings[i].equals("drop")) {
                    sendMsg = "code=" + strings[0] + "&type=" + strings[1];
                }
                if (strings[i].equals("getRecentDistance")) {
                    sendMsg = "code=" + strings[0] + "&type=" + strings[1];
                }
                if (strings[i].equals("updateRecentDistance")) {
                    sendMsg = "code=" + strings[0] + "&today=" + strings[1] + "&type=" + strings[2];
                }
            }

            osw.write(sendMsg);
            osw.flush();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                //boolean nextCheck = false;
                while ((str = reader.readLine()) != null) {
                    if (str.contains("<") || str.contains(">"))
                        continue;
                    else
                        buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                Log.i("connect fail", "통신 실패");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}