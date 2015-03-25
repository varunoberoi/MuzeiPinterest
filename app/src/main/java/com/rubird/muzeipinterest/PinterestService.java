package com.rubird.muzeipinterest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

import com.rubird.muzeipinterest.pojos.*;

/**
 * Created by varunoberoi on 20/03/15.
 */
interface PinterestService {
    @GET("/v3/pidgets/boards/{user}/{board}/pins/")
    PinterestResponse getWalls(@Path("user") String user, @Path("board") String board);
}