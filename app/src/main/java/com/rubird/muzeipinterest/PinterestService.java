package com.rubird.muzeipinterest;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import retrofit.http.GET;
import com.rubird.muzeipinterest.pojos.*;

/**
 * Created by varunoberoi on 20/03/15.
 */
interface PinterestService {
    @GET("/v3/pidgets/boards/qwert2099/test/pins/")
    PinterestResponse getWalls();
}