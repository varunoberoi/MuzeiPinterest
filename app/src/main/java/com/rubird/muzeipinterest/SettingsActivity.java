package com.rubird.muzeipinterest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.apps.muzei.api.MuzeiArtSource;


import java.text.DecimalFormat;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SettingsActivity extends Activity {

    public static final String PREFS_NAME = "main_prefs";
    private static final String TAG = SettingsActivity.class.getSimpleName();

    //Needed for Calligraphy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()

                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_settings);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();

        CheckBox wifiOnly = (CheckBox)findViewById(R.id.wifi);
        EditText username = (EditText)findViewById(R.id.username);
        EditText board = (EditText)findViewById(R.id.board);
        SeekBar frequency = (SeekBar) findViewById(R.id.frequency);
        final TextView frequencyLabel = (TextView) findViewById(R.id.labelFrequency2);

        final DecimalFormat df = new DecimalFormat("#.#");

        //Wifi status and setting
        wifiOnly.setChecked(settings.getBoolean(PreferenceKeys.WIFI_ONLY, false));

        wifiOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(PreferenceKeys.WIFI_ONLY, isChecked);
                editor.commit();
            }
        });

        username.setText(settings.getString(PreferenceKeys.USER_NAME, ""));
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString(PreferenceKeys.USER_NAME, s.toString());
                editor.commit();
            }
        });

        board.setText(settings.getString(PreferenceKeys.BOARD, ""));
        board.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString(PreferenceKeys.BOARD, s.toString());
                editor.commit();
            }
        });

        frequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = 2;
                if(progress == 100) {
                    frequencyLabel.setText("∞");
                    value = -1;
                }else {
                    value = 0.5f + (7.5f/99) * progress;
                    frequencyLabel.setText(df.format(value)+"hrs");
                }
                editor.putFloat(PreferenceKeys.FREQUENCY, value);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Float frequencyValue = settings.getFloat(PreferenceKeys.FREQUENCY, 2.0f);
        frequency.setProgress(frequencyValue == -1 ? 100 : (int)(99 * frequencyValue / 8));

        frequencyLabel.setText(df.format(frequencyValue)+"hrs");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
