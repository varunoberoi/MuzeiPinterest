
package com.rubird.muzeipinterest.pojos;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Board {

    @Expose
    private String description;
    @Expose
    private String url;
    @SerializedName("follower_count")
    @Expose
    private Integer followerCount;
    @SerializedName("image_thumbnail_url")
    @Expose
    private String imageThumbnailUrl;
    @SerializedName("pin_count")
    @Expose
    private Integer pinCount;
    @Expose
    private String id;
    @Expose
    private String name;

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The followerCount
     */
    public Integer getFollowerCount() {
        return followerCount;
    }

    /**
     * 
     * @param followerCount
     *     The follower_count
     */
    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    /**
     * 
     * @return
     *     The imageThumbnailUrl
     */
    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    /**
     * 
     * @param imageThumbnailUrl
     *     The image_thumbnail_url
     */
    public void setImageThumbnailUrl(String imageThumbnailUrl) {
        this.imageThumbnailUrl = imageThumbnailUrl;
    }

    /**
     * 
     * @return
     *     The pinCount
     */
    public Integer getPinCount() {
        return pinCount;
    }

    /**
     * 
     * @param pinCount
     *     The pin_count
     */
    public void setPinCount(Integer pinCount) {
        this.pinCount = pinCount;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

}
