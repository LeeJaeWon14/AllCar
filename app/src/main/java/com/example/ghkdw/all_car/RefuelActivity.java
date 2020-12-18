package com.example.ghkdw.all_car;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class RefuelActivity extends AppCompatActivity {
    TextView textDate, textLocation, textPrice;
    Button refuelRecordAdd;
    LinearLayout addRefuelLayout, parentLayout;
    String code;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel);

        code = getIntent().getStringExtra("code");

        textDate = (TextView)findViewById(R.id.textDate);
        textLocation = (TextView)findViewById(R.id.textLocation);
        textPrice = (TextView)findViewById(R.id.textPrice);
        refuelRecordAdd = (Button)findViewById(R.id.addRefuelRecord);
        addRefuelLayout = (LinearLayout)findViewById(R.id.addRefuelLayout);
        parentLayout = (LinearLayout)findViewById(R.id.parentLayout);

        addRefuelLayout.setOnLongClickListener(new CustomTouchListener(addRefuelLayout));

        loadRecord();

        refuelRecordAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = (View)View.inflate(RefuelActivity.this, R.layout.add_refuel_record_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(RefuelActivity.this);
                final AlertDialog dlg = builder.create();

                dlg.setView(dialogView);
                dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

                CalendarView calender = (CalendarView)dialogView.findViewById(R.id.calender);
                final EditText dateEdit = (EditText)dialogView.findViewById(R.id.add_refuel_edit_date);
                calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String date = year + "년 " + (month+1) + "월 " + dayOfMonth + "일";
                        dateEdit.setText(date);
                    }
                });

                final EditText locationEdit = (EditText)dialogView.findViewById(R.id.add_refuel_edit_location);
                final EditText priceEdit = (EditText)dialogView.findViewById(R.id.add_refuel_edit_price);

                Button addButton = (Button)dialogView.findViewById(R.id.add_refuel_button);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = dateEdit.getText().toString();
                        String location = locationEdit.getText().toString();
                        String price = priceEdit.getText().toString();


                        if(saveRecord(code, date, location, price)) {

                            if(parentLayout.getChildCount() == 1 && !flag) {
                                textDate.setText(date);
                                textLocation.setText(location);
                                textPrice.setText(price);
                                addRefuelLayout.addView(makeLine());
                                flag = true;
                                dlg.dismiss();
                            }
                            else {
                                LinearLayout layout = makeLayout();
                                layout.addView(makeLine());
                                layout.addView(makeTextView(date));
                                layout.addView(makeTextView(location));
                                layout.addView(makeTextView(price));
                                dlg.dismiss();
                            }



                            Toast.makeText(RefuelActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dlg.dismiss();
                            Toast.makeText(RefuelActivity.this, "저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dlg.show();
            }
        });
    }
    class CustomTouchListener implements View.OnLongClickListener {
        LinearLayout layout;
        CustomTouchListener(LinearLayout layout) {
            this.layout = layout;
        }
        @Override
        public boolean onLongClick(View v) {
            final View dialogView = (View)View.inflate(RefuelActivity.this, R.layout.only_ok_or_cancel_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(RefuelActivity.this);
            final AlertDialog dlg = builder.create();

            dlg.setView(dialogView);
            dlg.getWindow().setBackgroundDrawableResource(R.drawable.block);

            ((TextView)dialogView.findViewById(R.id.onlyCancelTitle_2)).setVisibility(View.INVISIBLE);
            ((TextView)dialogView.findViewById(R.id.onlyCancelTextView_2)).setText("삭제하시겠습니까?");

            ((Button)dialogView.findViewById(R.id.onlyOk)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteRecord(code, ((TextView)layout.getChildAt(1)).getText().toString(), ((TextView)layout.getChildAt(2)).getText().toString(), ((TextView)layout.getChildAt(3)).getText().toString())) {
                        dlg.dismiss();
                        LinearLayout parent = (LinearLayout)layout.getParent();
                        parent.removeView(layout);
                        Toast.makeText(RefuelActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RefuelActivity.this, "삭제 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();
                    }
                }
            });
            ((Button)dialogView.findViewById(R.id.onlyCancel_2)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
                }
            });

            dlg.show();

            return false;
        }
    }

    protected LinearLayout makeLayout() {
        LinearLayout layout = new LinearLayout(RefuelActivity.this);
        layout.setLayoutParams(addRefuelLayout.getLayoutParams());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setOnLongClickListener(new CustomTouchListener(layout));
        parentLayout.addView(layout);
        return layout;
    }

    protected TextView makeTextView(String msg) {
        final TextView textView = new TextView(RefuelActivity.this);
        textView.setText(msg);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(textLocation.getLayoutParams());
        textView.setTypeface(textLocation.getTypeface());
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(20);

        return textView;
    }

    protected TextView makeLine() {
        final TextView textView = new TextView(RefuelActivity.this);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(addRefuelLayout.getLayoutParams());
        textView.setBackground(getDrawable(R.drawable.line));

        return textView;
    }

    private void loadRecord() {
        String db_date = null, db_location = null, db_price = null;
        try {
            db_date = new RegisterActivity().execute(code, "loadRefuelRecordDate").get();
            db_location = new RegisterActivity().execute(code, "loadRefuelRecordLocation").get();
            db_price = new RegisterActivity().execute(code, "loadRefuelRecordPrice").get();
        } catch(NullPointerException e) {
            textDate.setVisibility(View.INVISIBLE); textLocation.setVisibility(View.INVISIBLE);
            textPrice.setText("기록 없음");
        } catch(Exception e) {
            e.printStackTrace();
        }

        String[] date = db_date.split("/");
        String[] location = db_location.split("/");
        String[] price = db_price.split("/");

        textDate.setText(date[0]);
        textLocation.setText(location[0]);
        textPrice.setText(price[0]);

        for(int i = 1; i < date.length; i++) {
            LinearLayout layout = makeLayout();
            layout.addView(makeLine());
            layout.addView(makeTextView(date[i]));
            layout.addView(makeTextView(location[i]));
            layout.addView(makeTextView(price[i]));
        }
    }

    private boolean deleteRecord(String code, String date, String location, String price) {
        String result = null;
        try {
            result = new RegisterActivity().execute(code, date, location, price, "deleteRefuelRecord").get();
            Log.i("결과 : ", result);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(result.contains("success")) return true;
        else return false;
    }

    private boolean saveRecord(String code, String date, String location, String price) {
        String result = null;
        try {
            result = new RegisterActivity().execute(code, date, location, price, "saveRefuelRecord").get();
            Log.i("결과 : ", result);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(result.contains("success")) return true;
        else return false;
    }
}
