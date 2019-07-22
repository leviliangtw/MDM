package org.ncsist.mdm;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.ncsist.mdm.RecordLookUpActivity.events;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        } else if (id == R.id.menu_back){
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || MDMMainPagePreferenceFragment.class.getName().equals(fragmentName)
                || MDMAboutPreferenceFragment.class.getName().equals(fragmentName)
                || MDMUpdateDBPreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MDMMainPagePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_mdmmainpage);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("VERSION"));
            bindPreferenceSummaryToValue(findPreference("BARRACKS"));
            bindPreferenceSummaryToValue(findPreference("SAFETAGNUM"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MDMAboutPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_mdmabout);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("AboutTitle"));
            bindPreferenceSummaryToValue(findPreference("RegisteredTime"));
            bindPreferenceSummaryToValue(findPreference("LockedTime"));
            bindPreferenceSummaryToValue(findPreference("UnlockedTime"));
            bindPreferenceSummaryToValue(findPreference("SettingFileVersion"));
            bindPreferenceSummaryToValue(findPreference("LockedPeriod"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MDMUpdateDBPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_mdmupdatedb);
            setHasOptionsMenu(true);

            final ListPreference UpdateEventList = (ListPreference) findPreference("UpdateEventList");
            final EditTextPreference UpdateRecordTime = (EditTextPreference)findPreference("UpdateRecordTime");
            final EditTextPreference UpdateUploadTime = (EditTextPreference)findPreference("UpdateUploadTime");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            String now = formatter.format(curDate);
            UpdateRecordTime.setText(now);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("UpdateEventList"));
            bindPreferenceSummaryToValue(findPreference("UpdateRecordTime"));
            bindPreferenceSummaryToValue(findPreference("UpdateUploadTime"));

            Preference confirmupdate = findPreference("ConfirmUpdate");
            confirmupdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    MDMDBHelper mdmdbhelper = new MDMDBHelper(getActivity(), "MDM.db", null, 1);
                    SQLiteDatabase db = mdmdbhelper.getWritableDatabase();
                    String UEL = UpdateEventList.getEntry().toString();
                    String URT = UpdateRecordTime.getText();
                    String UUT = UpdateUploadTime.getText();
                    mdmdbhelper.insertRecord(db, UEL, URT, UUT);

                    List<Record> result = mdmdbhelper.selectAllRecord(db);
                    Toast.makeText(getActivity(), "成功新增紀錄：\n" + UEL+ "，\n" + URT + "，\n" + UUT + "。", Toast.LENGTH_SHORT).show();
                    db.close();
                    return true;
                }
            });

            Preference deleteall = findPreference("DeleteAll");
            deleteall.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    MDMDBHelper mdmdbhelper = new MDMDBHelper(getActivity(), "MDM.db", null, 1);
                    SQLiteDatabase db = mdmdbhelper.getWritableDatabase();
                    mdmdbhelper.removeAllRecord(db);
                    db.close();
                    Toast.makeText(getActivity(), "成功清除資料庫", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            Preference add100RandomRecords = findPreference("Add100RandomRecords");
            add100RandomRecords.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    /*
                    MDMDBHelper mdmdbhelper = new MDMDBHelper(getActivity(), "MDM.db", null, 1);
                    SQLiteDatabase db = mdmdbhelper.getWritableDatabase();
                    mdmdbhelper.removeAllRecord(db);

                    List<Record> record_list = new ArrayList<Record>();
                    record_list.add(new Record(0,events[2],"2018/07/04 09:38:11","未上傳"));
                    record_list.add(new Record(0,events[3],"2018/07/04 11:00:00","未上傳"));
                    record_list.add(new Record(0,events[4],"2018/07/04 11:05:03","未上傳"));
                    record_list.add(new Record(0,events[1],"2018/07/04 21:02:01","未上傳"));
                    record_list.add(new Record(0,events[0],"2018/07/04 21:17:39","未上傳"));

                    for(int i=0; i<record_list.size(); i++){
                        mdmdbhelper.insertRecord(db,
                                record_list.get(i).getEvent(),
                                record_list.get(i).getRecordtime(),
                                record_list.get(i).getUploadtime());
                    }

                    db.close();
                    */
                    addDef100aultRecord(getActivity());
                    Toast.makeText(getActivity(), "成功新增百筆隨機紀錄", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    private static void addDef100aultRecord(Context context){
        MDMDBHelper mdmdbhelper = new MDMDBHelper(context, "MDM.db", null, 1);
        SQLiteDatabase db = mdmdbhelper.getWritableDatabase();
        // mdmdbhelper.removeAllRecord(db);

        List<Record> record_list = new ArrayList<Record>();

        for(int i=0; i<100; i++){
            record_list.add(new Record(0,
                    events[randBetween(0,4)],
                    randomDateTime(),
                    "未上傳"));
        }

        /*
        record_list.add(new Record(0,
                events[2],
                "2018/07/04 09:38:11",
                "未上傳"));
        record_list.add(new Record(0,
                events[3],
                "2018/07/04 11:00:00",
                "未上傳"));
        record_list.add(new Record(0,
                events[4],
                "2018/07/04 11:05:03",
                "未上傳"));
        record_list.add(new Record(0,
                events[1],
                "2018/07/04 21:02:01",
                "未上傳"));
        record_list.add(new Record(0,
                events[0],
                "2018/07/04 21:17:39",
                "未上傳"));
        */

        for(int i=0; i<record_list.size(); i++){
            mdmdbhelper.insertRecord(db,
                    record_list.get(i).getEvent(),
                    record_list.get(i).getRecordtime(),
                    record_list.get(i).getUploadtime());
        }

        db.close();
    }

    private static String randomDateTime(){
        Calendar calendar = Calendar.getInstance();
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault());
        calendar.setTime(curDate);
        int curyear = calendar.get(Calendar.YEAR);// Here you can set Range of years you need

        int year = randBetween(2016, curyear);// Here you can set Range of years you need
        int month = randBetween(0, 11);
        int hour = randBetween(9, 22); //Hours will be displayed in between 9 to 22
        calendar.set(year, month, 1);
        int day = randBetween(0, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int min = randBetween(0, 59);
        int sec = randBetween(0, 59);

        calendar.set(year, month, day, hour, min, sec);

        return formatter.format(calendar.getTime());
    }
    private static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
