package com.rubird.muzeipinterest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;
import com.rubird.muzeipinterest.pojos.Pin;
import com.rubird.muzeipinterest.pojos.PinterestResponse;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

/**
 * Created by varunoberoi on 20/03/15.
 */
public class PinterestArtSource extends RemoteMuzeiArtSource {

    private static final String TAG = "PinterestMuzei";
    private static final String SOURCE_NAME = "PinterestArtSource";

    public static final String ACTION_REFRESH = "com.rubird.muzeipinterest.REFRESH";
    private static final int PAGE_SIZE = 100;
    private HashMap<String, String> params = new HashMap<>();

    public PinterestArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }


    @Override
    protected void onTryUpdate(final int reason) throws RetryException {
        final SharedPreferences settings = getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
        final int rotateTimeMillis = getRotateTimeMillisFromSetting(settings.getFloat(PreferenceKeys.FREQUENCY, 2));

        String user, board;

        user = settings.getString(PreferenceKeys.PINTEREST_USER, "");
        board = settings.getString(PreferenceKeys.BOARD, "");

        // Don't execute main code until user & board are set
        if(user.isEmpty() || board.isEmpty()){
            if (BuildConfig.DEBUG) Log.d(TAG, "Refresh avoided: no user or/and board");
            if(rotateTimeMillis != -1)
                scheduleUpdate(System.currentTimeMillis() + rotateTimeMillis);
            return;
        }

        // Check if we cancel the update due to WIFI connection
        if (settings.getBoolean(PreferenceKeys.WIFI_ONLY, false) && !Utils.isWifiConnected(this)) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Refresh avoided: no wifi");
            if(rotateTimeMillis != -1)
                scheduleUpdate(System.currentTimeMillis() + rotateTimeMillis);
            return;
        }

        String BOARD_PINS_URL = "boards/" + board + "/pins/";
        params.put("limit", PAGE_SIZE+"");
        params.put("fields", "id,link,image,original_link,note");

        final String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;
        PDKClient client = PDKClient.configureInstance(this, Config.PINTEREST_APP_ID);
        client.setDebugMode(true);
        client.getPath(BOARD_PINS_URL, params, new PDKCallback(){
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(TAG, "onSuccess " + response.getPinList().size());

                Random random = new Random();
                String token;
                PDKPin pin = null;
                while (true) {
                    pin = response.getPinList().get(random.nextInt(PAGE_SIZE));
                    token = pin.getUid();
                    if (response.getPinList().size() <= 1 || !TextUtils.equals(token, currentToken)) {
                        break;
                    }
                }
                if(pin != null) {
                    String by = pin.getMetadata();

                    publishArtwork(new Artwork.Builder()
                            .title(pin.getNote())
                            .byline(by)
                            .imageUri(Uri.parse(pin.getImageUrl()))
                            .token(token)
                            .viewIntent(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(pin.getLink())))
                            .build());

                    scheduleUpdate(System.currentTimeMillis() + rotateTimeMillis);
                }
            }
        });
    }

    int getRotateTimeMillisFromSetting(float frequencySetting){
        return (int) (frequencySetting * 60 * 60 * 1000);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (ACTION_REFRESH.equals(action)) {
            scheduleUpdate(System.currentTimeMillis() + 1000);
            return;
        }
        super.onHandleIntent(intent);
    }
}
