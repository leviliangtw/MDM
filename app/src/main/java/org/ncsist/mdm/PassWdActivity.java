package org.ncsist.mdm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.jboss.aerogear.security.otp.api.MDMClock;

import org.ncsist.mdm.PassWordMaker.*;

import java.io.UnsupportedEncodingException;

public class PassWdActivity extends AppCompatActivity {

    public static final String[] UNLOCK_REASON = { "GPS訊號不佳", "移除裝置管理員", "嘗試移除裝置管理員", "申請解除MDM" };
    public static final String UNLOCK_REASON_UNLOCK = UNLOCK_REASON[0];
    public static final String UNLOCK_REASON_DEVICE_ADMIN_ON_DISABLE = UNLOCK_REASON[1];
    public static final String UNLOCK_REASON_DEVICE_ADMIN_ON_DISABLE_REQUESTED = UNLOCK_REASON[2];
    public static final String UNLOCK_REASON_UNINSTALL = UNLOCK_REASON[3];

    private RelativeLayout passLayout;
    private EditText editHint;
    private Button buttonGenerate;
    private TextView andPassword;
    private TextView iosPassword103;
    private TextView iosPassword102;

    private String barracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.activity_pass_wd);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultTitle = Integer.toString(R.string.default_version);
        String defaultBarracks = Integer.toString(R.string.default_barracks);
        barracks = sharedPreferences.getString("BARRACKS", defaultBarracks);

        passLayout = (RelativeLayout)findViewById(R.id.passLayout);
        editHint = (EditText)findViewById(R.id.hint);
        buttonGenerate = (Button)findViewById(R.id.generate);
        andPassword = (TextView)findViewById(R.id.andPasswd);
        iosPassword103 = (TextView)findViewById(R.id.iosPasswd103);
        iosPassword102 = (TextView)findViewById(R.id.iosPasswd102);

        passLayout.setFocusable(true);
        passLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passLayout.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(PassWdActivity.this.getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        editHint.setFocusable(true);
        editHint.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editHint.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editHint,InputMethodManager.SHOW_FORCED);
                return true;
            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassWordMaker and = new PassWordMaker();
                IosPassWordMaker ios = new IosPassWordMaker();
                String hint = editHint.getText().toString();
                if (!hint.equals("")) {
                    try {
                        andPassword.setText("Android: " + and.getUnlockPass(hint, barracks + ":" + UNLOCK_REASON_UNLOCK));
                        iosPassword103.setText("IOS 1.03+: " + ios.compute_password(barracks, hint));
                        iosPassword102.setText("IOS 1.02-: " + ios.compute_password_o(hint));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // listen APP SWITCH BUTTON -> USELESS
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Close The Keyboard
            // InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // imm.hideSoftInputFromWindow(PassWdActivity.this.getCurrentFocus().getWindowToken(), 0);
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
            return true;
        }
        return true;
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            // actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("解鎖 - 密碼");
        }
    }
}
