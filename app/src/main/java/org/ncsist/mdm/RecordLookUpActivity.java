package org.ncsist.mdm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.ncsist.mdm.R.id.recordlistview;
import static org.ncsist.mdm.R.layout.activity_record_look_up;

public class RecordLookUpActivity extends AppCompatActivity
        implements DateTimePickerFragment.MyDTPF_Listrner {

    private EditText startTime;
    private EditText endTime;
    private String now;
    private String yesterday;
    List<Record> selectrecord_list = new ArrayList<>();

    public static final String[] events = new String[] {
            "上鎖：常駐營區",
            "事件：開始解鎖",
            "事件：解鎖成功",
            "事件：自動上鎖開始定位",
            "事件：無法確認位置自動上鎖"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_record_look_up);
        setupActionBar();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        now = formatter.format(curDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.DATE, -1);
        yesterday = formatter.format(calendar.getTime());

        startTime = (EditText)findViewById(R.id.startTime);
        endTime = (EditText) findViewById(R.id.endTime);
        startTime.setInputType(InputType.TYPE_NULL);
        assert endTime != null;
        endTime.setInputType(InputType.TYPE_NULL);
        startTime.setText(yesterday);
        endTime.setText(now);

        lookUpRecord(startTime.getText().toString(), endTime.getText().toString());

        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* TODO Auto-generated method stub */
                if(hasFocus){
                    showDTPFDialog("startTime", yesterday);
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* TODO Auto-generated method stub */
                showDTPFDialog("startTime", yesterday);
            }
        });
        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* TODO Auto-generated method stub */
                if(hasFocus){
                    showDTPFDialog("endTime", now);

                }
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* TODO Auto-generated method stub */
                showDTPFDialog("endTime", now);
            }
        });

        Button lookUp = (Button)findViewById(R.id.lookUp);
        lookUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookUpRecord(startTime.getText().toString(), endTime.getText().toString());
            }
        });
    }

    @Override
    public void onPositiveClick(String et, int year, int month, int day, int hour, int min) {
        Log.d("Tag", "DialogFragment回傳的資料爲：" + year + "/" + month + "/" + day+ " " + hour + ":" + min);

        EditText thisET = (EditText) findViewById(getResources().getIdentifier(et,"id", getPackageName()));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min);
        String newtime = formatter.format(cal.getTime());
        assert thisET != null;
        thisET.setText(newtime);
    }

    @Override
    public void onNegativeClick(String et, int year, int month, int day, int hour, int min) {
        Log.d("TAG", "onNegativeClick!!");
    }

    public void showDTPFDialog(String et, String datetime)
    {
        DateTimePickerFragment dtpf = new DateTimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ET", et);
        bundle.putSerializable("DATETIME", datetime);
        dtpf.setArguments(bundle);
        dtpf.show(getSupportFragmentManager(), "dtpf");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setupActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("記錄查詢");
    }

    private void lookUpRecord(String start_time, String end_time){
        MDMDBHelper mdmdbhelper = new MDMDBHelper(RecordLookUpActivity.this, "MDM.db", null, 1);
        SQLiteDatabase db = mdmdbhelper.getWritableDatabase();
        selectrecord_list = mdmdbhelper.selectRecordBytime(db,
                start_time,
                end_time);
        db.close();

        ListView listV = (ListView) findViewById(recordlistview);
        MyAdapter adapter = new MyAdapter(RecordLookUpActivity.this, selectrecord_list);
        assert listV != null;
        listV.setAdapter(adapter);
    }
}
