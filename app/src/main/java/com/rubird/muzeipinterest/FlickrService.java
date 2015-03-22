package com.rubird.muzeipinterest;

import com.rubird.muzeipinterest.pojos.PinterestResponse;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by varunoberoi on 22/03/15.
 */
interface FlickrService {
    @GET("/services/rest/?method=flickr.photos.getInfo&nojsoncallback=1&format=json&api_key="+Config.FLICKR_API_KEY+"")
    FlickrResponse getImage(@Query("photo_id") String photo_id);
}

class FlickrResponse {
    Photo photo;
}

class Photo {
    String id;
    String originalsecret;
    String originalformat;
    String farm;
    String server;
    Title title;
    Owner owner;
}

class Title {
    String _content;
}

class Owner {
    String realname;
}
