package com.rubird.muzeipinterest;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.rubird.muzeipinterest.pojos.PinterestResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by varunoberoi on 22/03/15.
 */
public class Utils {

    static class OgTags {
        String image;
        String description;
        String title;
        String site_name;
    }

    // Parses og tags from webpage
    static OgTags parseOgTags(String webpageUrl) throws IOException {

        // A fix for 500px
        if(webpageUrl.contains("500px.com") && !webpageUrl.startsWith("https"))
            webpageUrl = webpageUrl.replace("http", "https");

        Document doc = Jsoup.connect(webpageUrl).get();

        OgTags tags = new OgTags();

        // Finding og:image
        Element ogImage = doc.select("meta[property=og:image]").first();
        if(ogImage != null) {
            tags.image = ogImage.attr("content");

            // Fixing protocol issue
            if(tags.image.startsWith("//"))
                tags.image = tags.image.replace("//", "http://");
        }

        Element ogDescription = doc.select("meta[property=og:description]").first();
        if(ogDescription != null)
            tags.description = ogDescription.attr("content");

        Element ogSiteName = doc.select("meta[property=og:site_name]").first();
        if(ogSiteName != null)
            tags.site_name = ogSiteName.attr("content");

        Element ogTitle = doc.select("meta[property=og:title]").first();
        if(ogTitle != null)
            tags.title = ogTitle.attr("content");
        else if(doc.title() != null)
            tags.title = doc.title();

        return tags;
    }

    // Check if url provided is an image url or not
    static boolean isUrlImage(String webpageUrl) throws IOException {
        URL url = new URL(webpageUrl);
        HttpURLConnection connection = (HttpURLConnection)  url.openConnection();
        connection.connect();
        if(connection.getContentType().contains("image")){
            return true;
        }
        return false;
    }

    // Parse flick photo id from url
    static String parseFlickrPhotoId(String url){
        Log.w("Utils", url);
        String s = url.toLowerCase();
        if(s.contains("photo.gne"))
            return Uri.parse(url).getQueryParameter("id");

        return s.split("flickr.com/photos/")[1].split("/")[1];
    }

    static OgTags parseFlickrUrl(String webpageUrl) throws RetrofitError{
        String photoId = parseFlickrPhotoId(webpageUrl);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.flickr.com")
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        Log.d("Retrofit", message);
                    }
                })
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError retrofitError) {
                        throw retrofitError;
                    }
                })
                .build();
        FlickrService service = restAdapter.create(FlickrService.class);
        FlickrResponse response = service.getImage(photoId);
        OgTags tags = new OgTags();
        tags.image = "https://farm" + response.photo.farm + ".staticflickr.com/" +  response.photo.server + "/" + response.photo.id + "_" + response.photo.originalsecret + "_o."+response.photo.originalformat;
        tags.site_name = response.photo.owner.realname;
        tags.title = response.photo.title._content;

        return tags;
    }

    /**
     * Determines if the WIFI is connected
     * @param context the needed Context
     * @return true if connected
     */
    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return  mWifi.isConnected();
    }

}
