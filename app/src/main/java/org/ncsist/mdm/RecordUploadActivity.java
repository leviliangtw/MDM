package org.ncsist.mdm;

import android.annotation.TargetApi;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RecordUploadActivity extends AppCompatActivity {

    private TextView recordNum;
    private TextView start_time;
    private TextView end_time;
    private ListView record5lists;
    private List<Record> allRecords;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_upload);

        recordNum = (TextView)findViewById(R.id.recordNum);
        start_time = (TextView)findViewById(R.id.start_time);
        end_time = (TextView)findViewById(R.id.end_time);
        record5lists = (ListView)findViewById(R.id.record5lists);

        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        String now = formatter.format(curDate);
        MDMDBHelper mdmdbhelper = new MDMDBHelper(RecordUploadActivity.this,
                "MDM.db", null, 1);
        SQLiteDatabase db = mdmdbhelper.getWritableDatabase();
        allRecords = mdmdbhelper.selectRecordBytime(db, "2016/01/01 00:00", now);
        List<Record> fiveRecords = new ArrayList<>();
        if(allRecords.size()>=5){
            recordNum.setText(String.valueOf(allRecords.size()));
            start_time.setText(allRecords.get(0).getRecordtime());
            end_time.setText(allRecords.get(allRecords.size() - 1).getRecordtime());
            for(int i=allRecords.size()-1; i>allRecords.size()-6; i--)
                fiveRecords.add(allRecords.get(i));
        }

        MyAdapter adapter = new MyAdapter(RecordUploadActivity.this, fiveRecords);
        assert record5lists != null;
        record5lists.setAdapter(adapter);

        upload = (Button)findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordUploadActivity.this, "無法連上註冊電腦，請確認是否已連上正確的無線網路。", Toast.LENGTH_SHORT).show();
            }
        });

        setupActionBar();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setupActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("記錄上傳");
    }
}
