
package com.rubird.muzeipinterest.pojos;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class User {

    @Expose
    private String about;
    @Expose
    private String location;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("follower_count")
    @Expose
    private Integer followerCount;
    @SerializedName("image_small_url")
    @Expose
    private String imageSmallUrl;
    @SerializedName("pin_count")
    @Expose
    private Integer pinCount;
    @Expose
    private String id;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;

    /**
     * 
     * @return
     *     The about
     */
    public String getAbout() {
        return about;
    }

    /**
     * 
     * @param about
     *     The about
     */
    public void setAbout(String about) {
        this.about = about;
    }

    /**
     * 
     * @return
     *     The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
     *     The imageSmallUrl
     */
    public String getImageSmallUrl() {
        return imageSmallUrl;
    }

    /**
     * 
     * @param imageSmallUrl
     *     The image_small_url
     */
    public void setImageSmallUrl(String imageSmallUrl) {
        this.imageSmallUrl = imageSmallUrl;
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
     *     The profileUrl
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * 
     * @param profileUrl
     *     The profile_url
     */
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

}
