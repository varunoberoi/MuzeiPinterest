
package com.rubird.muzeipinterest.pojos;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Pin {

    @Expose
    private Attribution attribution;
    @Expose
    private String description;
    @Expose
    private Pinner pinner;
    @SerializedName("repin_count")
    @Expose
    private Integer repinCount;
    @SerializedName("dominant_color")
    @Expose
    private String dominantColor;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;
    @Expose
    private String link;
    @Expose
    private Images images;
    @Expose
    private Object embed;
    @SerializedName("is_video")
    @Expose
    private Boolean isVideo;
    @Expose
    private String id;

    /**
     * 
     * @return
     *     The attribution
     */
    public Attribution getAttribution() {
        return attribution;
    }

    /**
     * 
     * @param attribution
     *     The attribution
     */
    public void setAttribution(Attribution attribution) {
        this.attribution = attribution;
    }

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
     *     The pinner
     */
    public Pinner getPinner() {
        return pinner;
    }

    /**
     * 
     * @param pinner
     *     The pinner
     */
    public void setPinner(Pinner pinner) {
        this.pinner = pinner;
    }

    /**
     * 
     * @return
     *     The repinCount
     */
    public Integer getRepinCount() {
        return repinCount;
    }

    /**
     * 
     * @param repinCount
     *     The repin_count
     */
    public void setRepinCount(Integer repinCount) {
        this.repinCount = repinCount;
    }

    /**
     * 
     * @return
     *     The dominantColor
     */
    public String getDominantColor() {
        return dominantColor;
    }

    /**
     * 
     * @param dominantColor
     *     The dominant_color
     */
    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    /**
     * 
     * @return
     *     The likeCount
     */
    public Integer getLikeCount() {
        return likeCount;
    }

    /**
     * 
     * @param likeCount
     *     The like_count
     */
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * 
     * @return
     *     The link
     */
    public String getLink() {
        return link;
    }

    /**
     * 
     * @param link
     *     The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 
     * @return
     *     The images
     */
    public Images getImages() {
        return images;
    }

    /**
     * 
     * @param images
     *     The images
     */
    public void setImages(Images images) {
        this.images = images;
    }

    /**
     * 
     * @return
     *     The embed
     */
    public Object getEmbed() {
        return embed;
    }

    /**
     * 
     * @param embed
     *     The embed
     */
    public void setEmbed(Object embed) {
        this.embed = embed;
    }

    /**
     * 
     * @return
     *     The isVideo
     */
    public Boolean getIsVideo() {
        return isVideo;
    }

    /**
     * 
     * @param isVideo
     *     The is_video
     */
    public void setIsVideo(Boolean isVideo) {
        this.isVideo = isVideo;
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

}
