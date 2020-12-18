package com.example.ghkdw.all_car;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by ghkdw on 2019-10-17.
 */

public class SuspensionFragment extends Fragment {
    View view;
    Context context;
    ListView listView;
    String code;

    public static SuspensionFragment newInstance(int page, String title, String code) {
        SuspensionFragment fragment = new SuspensionFragment();
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
        view = inflater.inflate(R.layout.fragment_suspension, container, false);
        context = container.getContext();

        listView = (ListView)view.findViewById(R.id.listItems_suspension);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final String[] items = getResources().getStringArray(R.array.suspension);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final View dialogView = (View)View.inflate(context, R.layout.repair_dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                TextView name = (TextView)dialogView.findViewById(R.id.repair_name);
                name.setText(items[position]);

                ProgressBar prgb = (ProgressBar)dialogView.findViewById(R.id.repair_progress);

                TextView repairView = (TextView)dialogView.findViewById(R.id.repair_history);
                String result = null;
                try {
                    RegisterActivity task = new RegisterActivity();
                    result = task.execute(code, items[position], "loadRecord").get();
                    Log.i("결과 : ", result);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                String[] history = result.split("/");
                if(history[0].contains("없음")) { }
                else {
                    prgb.setVisibility(View.INVISIBLE);
                    repairView.setVisibility(View.VISIBLE);
                    repairView.setText("마지막 정비 : " + history[history.length-1] + "km");
                }

                Button prevRecord = (Button)dialogView.findViewById(R.id.prev_repair_button);
                prevRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View dialogView = (View)View.inflate(context, R.layout.only_ok_or_cancel_dialog, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        final AlertDialog dlg = builder.create();

                        dlg.setView(dialogView);
                        dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                        TextView title = (TextView)dialogView.findViewById(R.id.onlyCancelTitle_2);
                        title.setText(items[position]);

                        String result = null;
                        try {
                            RegisterActivity task = new RegisterActivity();
                            result = task.execute(code, items[position], "loadRecord").get();
                            Log.i("결과", result);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        String[] history = result.split("/");
                        TextView msg = (TextView)dialogView.findViewById(R.id.onlyCancelTextView_2);
                        msg.setText("");
                        if(history[0].contains("없음")) {
                            msg.setText("정비 내역 없음");
                        }
                        else {
                            for(int i = 0; i < history.length; i++) {
                                msg.setText(msg.getText().toString() + (i+1) + "번째 정비 : " + history[i] + "km" + "\r\n");
                            }
                        }

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
                });

                Button ok = (Button)dialogView.findViewById(R.id.repair_button);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });

                dlg.show();
            }
        });
    }
}
