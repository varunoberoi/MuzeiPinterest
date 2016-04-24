package com.rubird.muzeipinterest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.muzei.api.MuzeiArtSource;
import com.pinterest.android.pdk.PDKBoard;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SettingsActivity extends Activity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    public static final String PREFS_NAME = "main_prefs";
    private static boolean changed = false;
    private PDKClient pdkClient;
    private Button loginWithPinterestButton, logoutPinterest;
    private CircleImageView profilePicture;
    private TextView username;
    private ViewGroup settingsViewGroup;

    private Spinner pinterestBoardList;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private PDKUser profile;

    private List<String> boardNames = new ArrayList<>();
    private List<PDKBoard> boards;

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

        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();

        settingsViewGroup = (ViewGroup) findViewById(R.id.linearLayout);

        loginWithPinterestButton = (Button) findViewById(R.id.login_with_pinterest);
        loginWithPinterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        logoutPinterest = (Button) findViewById(R.id.logout_pinterest);
        logoutPinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
            }
        });

        pinterestBoardList = (Spinner) findViewById(R.id.pinterest_board_list);

        profilePicture = (CircleImageView) findViewById(R.id.profilePic);
        username = (TextView) findViewById(R.id.usernameTV);

        CheckBox wifiOnly = (CheckBox) findViewById(R.id.wifi);
        EditText username = (EditText) findViewById(R.id.username);
        EditText board = (EditText) findViewById(R.id.board);
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
                changed = true;
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
                changed = true;
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
                changed = true;
            }
        });

        frequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = 2;
                if (progress == 100) {
                    frequencyLabel.setText("∞");
                    value = -1;
                } else {
                    value = 0.5f + (7.5f / 99) * progress;
                    frequencyLabel.setText(df.format(value) + "hrs");
                }
                editor.putFloat(PreferenceKeys.FREQUENCY, value);
                editor.commit();
                changed = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pinterestBoardList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(PreferenceKeys.BOARD, getIdFromName(boardNames.get(position)));
                editor.commit();
                changed = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Float frequencyValue = settings.getFloat(PreferenceKeys.FREQUENCY, 2.0f);
        frequency.setProgress(frequencyValue == -1 ? 100 : (int) (99 * frequencyValue / 8));

        frequencyLabel.setText(df.format(frequencyValue) + "hrs");

        pdkClient = PDKClient.configureInstance(this, Config.PINTEREST_APP_ID);
        pdkClient.onConnect(this);
        pdkClient.setDebugMode(true);

        fetchProfileFromPreferences();
        switchUI();

        pdkClient.getBoardPins(settings.getString(PreferenceKeys.BOARD, ""), "id,link,image,original_link,note", new PDKCallback(){
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(TAG, response.getPinList().size()+" pins found");
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        //Launch an update if a source has been added
        if (changed) {
            Intent intent = new Intent(PinterestArtSource.ACTION_REFRESH);
            intent.setClass(this, PinterestArtSource.class);
            startService(intent);
        }
    }

    private void onLogin() {
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PRIVATE);

        pdkClient.login(this, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                pdkClient.getMe("first_name,last_name,image", new PDKCallback() {
                    @Override
                    public void onSuccess(PDKResponse response) {
                        Log.d(TAG, response.getData().toString());
                        Log.d(TAG, response.getUser().toString());
                        profile = response.getUser();

                        editor.putString(PreferenceKeys.PINTEREST_USER, response.getData().toString());
                        editor.commit();
                        switchUI();
                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        Log.e(getClass().getName(), exception.getDetailMessage());
                    }
                });
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });

    }

    private void onLogout() {
        pdkClient.logout();
        editor.remove(PreferenceKeys.PINTEREST_USER);
        editor.commit();
        profile = null;
        switchUI();
    }

    private void switchUI() {
        if (profile != null) {
            settingsViewGroup.setVisibility(View.VISIBLE);
            logoutPinterest.setVisibility(View.VISIBLE);
            loginWithPinterestButton.setVisibility(View.GONE);
            username.setText(profile.getFirstName() + " " + profile.getLastName());
            String higherResProfilePicUrl = profile.getImageUrl().replace("_60", "_280");  // #hack
            Picasso.with(SettingsActivity.this).load(higherResProfilePicUrl).into(profilePicture);
            setBoards();
        } else {
            settingsViewGroup.setVisibility(View.GONE);
            logoutPinterest.setVisibility(View.GONE);
            loginWithPinterestButton.setVisibility(View.VISIBLE);
        }
    }

    private void setBoards() {
        pdkClient.getMyBoards("id,name", new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.toString());
                boards = response.getBoardList();

                boardNames.clear();

                for (PDKBoard board : boards) {
                    boardNames.add(board.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_spinner_item, boardNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pinterestBoardList.setAdapter(adapter);

                String selectedBoard = settings.getString(PreferenceKeys.BOARD, "");
                if(!selectedBoard.isEmpty()){
                    int pos = -1;
                    for(int i=0; i<boards.size(); i++){
                        if(selectedBoard.equals(boards.get(i).getUid())){
                            pos = i;
                            break;
                        }
                    }
                    pinterestBoardList.setSelection(pos);
                }
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
                Toast.makeText(SettingsActivity.this, "My boards Error ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchProfileFromPreferences() {
        if (!settings.contains(PreferenceKeys.PINTEREST_USER)) {
            profile = null;
            return;
        }

        String profileString = settings.getString(PreferenceKeys.PINTEREST_USER, "");

        try {
            profile = PDKUser.makeUser(new JSONObject(profileString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getIdFromName(String name){
        String id = null;
        for (PDKBoard board : boards) {
            if(board.getName().equals(name))
                return board.getUid();
        }
        return id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PDKClient.getInstance().onOauthResponse(requestCode, resultCode, data);
    }
}
