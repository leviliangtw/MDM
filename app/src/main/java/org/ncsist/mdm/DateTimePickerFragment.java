package org.ncsist.mdm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateTimePickerFragment extends DialogFragment {

    private Date mDate;
    private int year, month, day, hour, min;
    private MyDTPF_Listrner myDTPF_Listrner;
    private String et, datetime;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myDTPF_Listrner = (MyDTPF_Listrner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // mDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        Calendar calendar = Calendar.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            et = bundle.getString("ET");
            datetime = bundle.getString("DATETIME");
            Log.d("Tag", "EditText: " + et );
            Log.d("Tag", "DateTime: " + datetime );
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            mDate = formatter.parse(datetime);
            System.out.println(mDate);
            calendar.setTime(mDate);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_date_time_picker, null);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker)v.findViewById(R.id.timePicker);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                DateTimePickerFragment.this.year = year;
                DateTimePickerFragment.this.month = month;
                DateTimePickerFragment.this.day = day;
                // updateDateTime();
            }
        });

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hour, int min) {
                DateTimePickerFragment.this.hour = hour;
                DateTimePickerFragment.this.min = min;
                // updateDateTime();
            }
        });

        builder.setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myDTPF_Listrner.onPositiveClick(et, year, month, day, hour, min);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myDTPF_Listrner.onNegativeClick(et, year, month, day, hour, min);
                        dismiss();// cancel
                    }
                });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        // DialogFragment關閉時回傳資料给Activity
        // 通過回呼介面回傳資料给Activity
        //if (MyDTPF_Listrner != null) {
          //  MyDTPF_Listrner.onPositiveClick(year, month, day, hour, min);
        //}
        super.onDestroy();
    }

    public interface MyDTPF_Listrner {
        void onPositiveClick(String et, int year, int month, int day, int hour, int min);
        void onNegativeClick(String et, int year, int month, int day, int hour, int min);
    }
}
