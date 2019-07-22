package org.ncsist.mdm;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView state;
    private TextView currentBaracks;
    private Button buttonLock;
    private Button buttonUnLock;
    private RelativeLayout mainLayout;
    private TextClock mTextClock;

    private String title;
    private String barracks;
    private String safetagnum;
    private String now;
    private String abouttitle;
    private String registeredtime;
    private String lockedtime;
    private String unlockedtime;
    private String settingfileversion;
    private String lockedperiod;

    public static final String[] recentBaracks = new String[] {
            "福興營區",
            "忠勇營區",
            "美崙山",
            "四支部",
            "佳山營區",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        now = formatter.format(curDate);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.mipmap.ic_launcher);
        // Toast.makeText(this, title, Toast.LENGTH_LONG).show();
        //toolbar.setTitle(title);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        setSupportActionBar(toolbar);

        state = (TextView)findViewById(R.id.State);

        currentBaracks = (TextView)findViewById(R.id.currentBaracks);

        buttonLock = (Button)findViewById(R.id.lock);
        buttonUnLock = (Button)findViewById(R.id.unlock);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentBaracks.getText().equals("未上鎖") || currentBaracks.getText().equals("解鎖中")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
                    dialog.setTitle("上鎖 - 常駐營區");
                    dialog.setMessage("您確定要上鎖嗎？");
                    dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            mainLayout.setBackgroundColor(Color.GREEN);
                            state.setText("已上鎖，可進入" + barracks);
                            currentBaracks.setText(barracks);
                            Toast.makeText(MainActivity.this, "進入管制模式！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                } else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
                    dialog.setTitle("已經上鎖");
                    dialog.setMessage("裝置已經上鎖。");
                    dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                        }
                    });
                    dialog.show();
                }
            }
        });
        buttonUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentBaracks.getText().equals("未上鎖")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
                    dialog.setTitle("已經解鎖");
                    dialog.setMessage("裝置已經解鎖。");
                    dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                        }
                    });
                    dialog.show();
                } else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
                    dialog.setTitle("解鎖");
                    dialog.setMessage("您確定要解鎖嗎？");
                    dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            //Toast.makeText(MainActivity.this, "離開", Toast.LENGTH_SHORT).show();
                        }

                    });
                    dialog.setNegativeButton("僅GPS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            mainLayout.setBackgroundColor(Color.YELLOW);
                            state.setText("解鎖中，尚無定位資訊");
                            currentBaracks.setText("解鎖中");
                            Toast.makeText(MainActivity.this, "將以定位判斷是否離開管制區！", Toast.LENGTH_SHORT).show();

                            // 等待二秒
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mainLayout.setBackgroundColor(Color.RED);
                                    state.setText("未上鎖，不得進入管制營區");
                                    currentBaracks.setText("未上鎖");
                                }
                            }, 2000);
                        }
                    });
                    dialog.setPositiveButton("快速", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            mainLayout.setBackgroundColor(Color.YELLOW);
                            state.setText("解鎖中，尚無定位資訊");
                            currentBaracks.setText("解鎖中");
                            Toast.makeText(MainActivity.this, "將以定位判斷是否離開管制區！", Toast.LENGTH_SHORT).show();

                            // 等待二秒
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mainLayout.setBackgroundColor(Color.RED);
                                    state.setText("未上鎖，不得進入管制營區");
                                    currentBaracks.setText("未上鎖");
                                }
                            },2000);
                        }
                    });
                    dialog.show();
                }


            }
        });

        //mTextClock = findViewById(R.id.textClock);
        //mTextClock.setFormat24Hour("(yyyy-MM-dd HH:mm:ss)");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 偏好設定
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_lockUsualBaracks){

        } else if (id == R.id.action_lockNewBaracksQRCode){

        } else if (id == R.id.action_lockNewBaracksWiFi){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("上鎖 - 新營區(WiFi)");
            dialog.setMessage("您確定要以新營區座標上鎖嗎？");
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "請檢查是否已連上註冊主機之 WiFi 熱點！", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else if (id == R.id.action_lockRecentBaracks){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("上鎖後列表中營區均不能解鎖：");

            String[] recentBaracksList = new String[recentBaracks.length];
            for(int i=0;i<recentBaracks.length;i++) {
                recentBaracksList[i] = (i + ": " + recentBaracks[i]);
            }
            dialog.setSingleChoiceItems(recentBaracksList, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("BARRACKS", recentBaracks[which]);
                    editor.commit();
                }
            });

            // dialog.setMessage("0: 公館營區");
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("上鎖", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    barracks = sharedPreferences.getString("BARRACKS", Integer.toString(R.string.default_barracks));
                    Toast.makeText(MainActivity.this, "選擇以"+ barracks + "營區上鎖。", Toast.LENGTH_SHORT).show();
                    onResume();
                }
            });
            dialog.show();
        } else if (id == R.id.action_lockMoveBaracks){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("上鎖 - 船艦模式(1/3)：");
            dialog.setMessage("以船艦模式上鎖後，需執行轉換一般營區、定點解鎖或上鎖-新營區(WiFi)才能進行解鎖。\n" +
                    "您確定要以船艦模式上鎖嗎？");
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("繼續", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                }
            });
            dialog.show();
        } else if (id == R.id.action_transCommBaracks){

        } else if (id == R.id.action_unlockFixLocation){

        } else if (id == R.id.action_unlockByPasswd) {
            startActivity(new Intent(this, PassWdActivity.class));
            this.finish();
            return true;
        } else if (id == R.id.action_GPSStatus){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("定位狀況查詢");
            dialog.setMessage("尚無定位資訊");
            dialog.setNegativeButton("離開", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else if (id == R.id.action_fakeGPSInspect){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("手機有偽冒定位功能APP，可能會影響解鎖：");
            dialog.setMessage("圖片庫");
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(MainActivity.this, "未選擇營區，無法上鎖。", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else if (id == R.id.action_serviceGPS){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("GPS");
            dialog.setMessage("您確定要進行GPS設定嗎？");
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(MainActivity.this, "未選擇營區，無法上鎖。", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            dialog.show();
        } else if (id == R.id.action_settingOfAutoLock){
            startActivity(new Intent(this, SettingOfAutoLockActivity.class));
            // this.finish();
            return true;
        } else if (id == R.id.action_recordUploadByWiFi){
            startActivity(new Intent(this, RecordUploadActivity.class));
            // this.finish();
            return true;

        } else if (id == R.id.action_recordLookUp) {
            startActivity(new Intent(this, RecordLookUpActivity.class));
            // this.finish();
            return true;
        } else if (id == R.id.action_checkVersionByWifi){
            Toast.makeText(MainActivity.this, "請檢查是否已連上註冊主機之 WiFi 熱點！", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_updateMDMsettingByWifi){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("更新MDM設定");
            dialog.setMessage("您確定要更新MDM設定嗎？");
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "無法更新MDM設定:無法連上註冊電腦，請檢查是否已連上正確的無線網路。", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else if (id == R.id.action_RegisterByWiFi){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("手機註冊");
            dialog.setMessage("您確定要進行手機註冊嗎？");
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "請檢查是否已連上註冊主機之 WiFi 熱點！", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else if (id == R.id.action_about) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("關於程式");
            dialog.setMessage(
                            abouttitle + "\n" +
                            "\n" +
                            "啟動時間: " + now + "\n" +
                            "\n" +
                            "註冊日期: " + registeredtime + "\n" +
                            "\n" +
                            "上鎖類別: 常駐營區上鎖\n" +
                            "上鎖日期: " + lockedtime  + "\n" +
                            "\n" +
                            "解鎖類別: 解鎖成功\n" +
                            "解鎖日期: " + unlockedtime + "\n" +
                            "\n" +
                            "尚無資料上傳紀錄" +
                            "\n" +
                            "\n" +
                            "設定檔版本: \n" +
                            settingfileversion + "\n" +
                            "\n" +
                            "系統設定內容\n" +
                            "  常駐營區: " + barracks + "\n" +
                            "  管制項目: \n" +
                            "    相機\n" +
                            "    定位\n" +
                            "    熱點分享\n" +
                            "    藍芽\n" +
                            "  自動上鎖啟用中\n" +
                            "    週一上鎖時段: " + lockedperiod + "\n" +
                            "    週二上鎖時段: " + lockedperiod + "\n" +
                            "    週三上鎖時段: " + lockedperiod + "\n" +
                            "    週四上鎖時段: " + lockedperiod + "\n" +
                            "    週五上鎖時段: " + lockedperiod + "\n" +
                            "    週六無自動上鎖\n" +
                            "    週日無自動上鎖\n"
            );
            dialog.setNeutralButton("取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "離開", Toast.LENGTH_SHORT).show();
                }

            });
            dialog.show();
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_leave) {
            this.finish();
            return true;
        } else if (id == R.id.action_remove) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
            dialog.setTitle("解除APP");
            dialog.setMessage("您確定要解除APP嗎？");
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(MainActivity.this, "我還尚未了解", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                }
            });
            dialog.show();
        } else if (id == R.id.connectionTest) {
            Uri uri = Uri.parse("http://192.168.2.2/mdm/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 偏好設定
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String defaultTitle = Integer.toString(R.string.default_version);
        String defaultBarracks = Integer.toString(R.string.default_barracks);
        String defaultSafetagnum = Integer.toString(R.string.default_safetagnum);
        String defaultAboutTitle = Integer.toString(R.string.default_abouttitle);
        String defaultRegisteredTime = Integer.toString(R.string.default_registeredtime);
        String defaultLockedTime = Integer.toString(R.string.default_lockedtime);
        String defaultUnlockedTime = Integer.toString(R.string.default_unlockedtime);
        String defaultSettingFileVersion = Integer.toString(R.string.default_settingfileversion);
        String defaultLockedPeriod = Integer.toString(R.string.default_lockedperiod);

        title = sharedPreferences.getString("VERSION", defaultTitle);
        barracks = sharedPreferences.getString("BARRACKS", defaultBarracks);
        safetagnum = sharedPreferences.getString("SAFETAGNUM", defaultSafetagnum);
        abouttitle = sharedPreferences.getString("AboutTitle", defaultAboutTitle);
        registeredtime  = sharedPreferences.getString("RegisteredTime", defaultRegisteredTime);
        lockedtime = sharedPreferences.getString("LockedTime", defaultLockedTime);
        unlockedtime = sharedPreferences.getString("UnlockedTime", defaultUnlockedTime);
        settingfileversion = sharedPreferences.getString("SettingFileVersion", defaultSettingFileVersion);
        lockedperiod = sharedPreferences.getString("LockedPeriod", defaultLockedPeriod);

        TextView saveTagNum = (TextView)findViewById(R.id.saveTagNum);


        toolbar.setTitle(title);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        setSupportActionBar(toolbar);

        state.setText("已上鎖，可進入" + barracks);
        currentBaracks.setText(barracks);
    }
}
