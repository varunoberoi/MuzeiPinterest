package com.rubird.muzeipinterest;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import retrofit.http.GET;
import com.rubird.muzeipinterest.pojos.*;

/**
 * Created by varunoberoi on 20/03/15.
 */
interface PinterestService {
    @GET("/v3/pidgets/boards/qwert2099/desktop-wallpapers/pins/")
    PinterestResponse getWalls();

//    static class WallsResponse {
//        List<Wall> photos;
//    }
//
//    static class Wall {
//        int id;
//        String link;
//        ImagesObject images;
//        Attribution attribution;
//    }
//
//    static class Attribution {
//        String title;
//        String author_name;
//    }
//
//
//    static class ImagesObject {
//        @SerializedName("237x")
//        SizeObject size;
//    }
//
//    static class SizeObject {
//        String url;
//    }
}