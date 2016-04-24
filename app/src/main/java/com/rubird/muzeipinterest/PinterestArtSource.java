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

//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint("https://api.pinterest.com")
//                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog(TAG))
//                .setErrorHandler(new ErrorHandler() {
//                    @Override
//                    public Throwable handleError(RetrofitError retrofitError) {
//                        if(retrofitError.getResponse() == null){
//                            Log.d(TAG, "inside error handler retry exception as no response");
//                            return retrofitError;
//                        }
//                        int statusCode = retrofitError.getResponse().getStatus();
//                        if (retrofitError.getKind() == RetrofitError.Kind.NETWORK
//                                || (500 <= statusCode && statusCode < 600)) {
//                            Log.d(TAG, "inside error handler retry exception");
//                            return new RetryException();
//                        }
//                        if(rotateTimeMillis != -1)
//                            scheduleUpdate(System.currentTimeMillis() + rotateTimeMillis);
//                        Log.d(TAG, "inside error handler returning error");
//                        return retrofitError;
//                    }
//                })
//                .build();
//
//        PinterestService service = restAdapter.create(PinterestService.class);
//        PinterestResponse response;
//
//        try {
//            response = service.getWalls(user, board);
//        }catch(RetrofitError e){
//            e.printStackTrace();
//            throw new RetryException();
//        }
//
//        if (response == null || response.getData().getPins() == null) {
//            throw new RetryException();
//        }
//
//        if (response.getData().getPins().size() == 0) {
//            Log.w(TAG, "No photos returned from API.");
//            scheduleUpdate(System.currentTimeMillis() + rotateTimeMillis);
//            return;
//        }
//
//        Random random = new Random();
//        Pin pin;
//        String token;
//        while (true) {
//            pin = response.getData().getPins().get(random.nextInt(response.getData().getPins().size()));
//            token = pin.getId();
//            if (response.getData().getPins().size() <= 1 || !TextUtils.equals(token, currentToken)) {
//                break;
//            }
//        }
//
//        String title, authorName, photoUrl;
//
//        // A fix (in case pin's link is null)
//        if(pin.getLink() == null)
//            pin.setLink("");
//
//        // Handling Flickr URLs seperately
//        if(pin.getLink().contains("flickr.com")){
//            Utils.OgTags tags = Utils.parseFlickrUrl(pin.getLink());
//            photoUrl = tags.image;
//            title = tags.title != null ? tags.title : tags.description;
//            authorName = tags.site_name != null ? tags.site_name : "No Author Available";
//        }else if(pin.getLink().contains("vsco.co/media")) {
//            Utils.OgTags tags = null;
//            try {
//                tags = Utils.parseOgTags(pin.getLink());
//                title = tags.title != null ? tags.title : tags.description;
//                authorName = tags.site_name != null ? tags.site_name : "No Author Available";
//                photoUrl = tags.image.replaceAll("(\\/\\d+x\\d+\\/)", "/");
//            } catch (IOException e) {
//                e.printStackTrace();
//                title = pin.getDescription();
//                authorName = "No Author Available";
//                photoUrl = pin.getImages().get237x().getUrl().replace("237x", "originals");
//            }
//        }else {
//            // HQ images from pinterest
//            photoUrl = pin.getImages().get237x().getUrl().replace("237x", "originals");
//
//            // If attribution available using that
//            if(pin.getAttribution() != null) {
//                authorName = pin.getAttribution().getAuthorName();
//                title = pin.getAttribution().getTitle();
//            }else{
//
//                // Handling original pinterest images
//                if(pin.getLink().isEmpty()){
//                    authorName = pin.getPinner().getFullName();
//                    title = pin.getDescription();
//                }else {
//                    // For all other sources parse out og meta tags coz you have to acknowledge the source :)
//                    Utils.OgTags tags = null;
//                    try {
//                        tags = Utils.parseOgTags(pin.getLink());
//                        title = tags.title != null ? tags.title : tags.description;
//                        title = title != null ? title : pin.getDescription();
//                        authorName = tags.site_name != null ? tags.site_name : "No Author Available";
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        title = pin.getDescription();
//                        authorName = "No Author Available";
//                    }
//                }
//            }
//        }

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
