package com.rubird.muzeipinterest;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rubird.muzeipinterest.pojos.Pin;
import com.rubird.muzeipinterest.pojos.PinterestResponse;

import java.util.Random;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * Created by varunoberoi on 20/03/15.
 */
public class PinterestArtSource extends RemoteMuzeiArtSource {

    private static final String TAG = "PinterestMuzei";
    private static final String SOURCE_NAME = "PinterestArtSource";

    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours

    public PinterestArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }


    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.pinterest.com")
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError retrofitError) {
                        Log.e(TAG, "THrowing Error");
                        int statusCode = retrofitError.getResponse().getStatus();
                        if (retrofitError.getKind() == RetrofitError.Kind.NETWORK
                                || (500 <= statusCode && statusCode < 600)) {
                            return new RetryException();
                        }
                        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                        return retrofitError;
                    }
                })
                .build();

        PinterestService service = restAdapter.create(PinterestService.class);
        PinterestResponse response = service.getWalls();

        if (response == null || response.getData().getPins() == null) {
            throw new RetryException();
        }

        if (response.getData().getPins().size() == 0) {
            Log.w(TAG, "No photos returned from API.");
            scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
            return;
        }

        Log.v(TAG, "We have "+response.getData().getPins().size()+" photos");
        Random random = new Random();
        Pin pin;
        String token;
        while (true) {
            pin = response.getData().getPins().get(random.nextInt(response.getData().getPins().size()));
            token = pin.getId();
            if (response.getData().getPins().size() <= 1 || !TextUtils.equals(token, currentToken)) {
                break;
            }
        }

        publishArtwork(new Artwork.Builder()
                .title(pin.getAttribution().getTitle())
                .byline(pin.getAttribution().getAuthorName())
                .imageUri(Uri.parse(pin.getImages().get237x().getUrl()))
                .token(token)
                .viewIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(pin.getLink())))
                .build());

        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
    }
}
