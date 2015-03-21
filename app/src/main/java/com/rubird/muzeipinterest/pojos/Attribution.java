
package com.rubird.muzeipinterest.pojos;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Attribution {

    @Expose
    private String title;
    @Expose
    private String url;
    @SerializedName("provider_icon_url")
    @Expose
    private String providerIconUrl;
    @SerializedName("author_name")
    @Expose
    private String authorName;
    @SerializedName("provider_favicon_url")
    @Expose
    private String providerFaviconUrl;
    @SerializedName("author_url")
    @Expose
    private String authorUrl;
    @SerializedName("provider_name")
    @Expose
    private String providerName;

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
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
     *     The providerIconUrl
     */
    public String getProviderIconUrl() {
        return providerIconUrl;
    }

    /**
     * 
     * @param providerIconUrl
     *     The provider_icon_url
     */
    public void setProviderIconUrl(String providerIconUrl) {
        this.providerIconUrl = providerIconUrl;
    }

    /**
     * 
     * @return
     *     The authorName
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * 
     * @param authorName
     *     The author_name
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * 
     * @return
     *     The providerFaviconUrl
     */
    public String getProviderFaviconUrl() {
        return providerFaviconUrl;
    }

    /**
     * 
     * @param providerFaviconUrl
     *     The provider_favicon_url
     */
    public void setProviderFaviconUrl(String providerFaviconUrl) {
        this.providerFaviconUrl = providerFaviconUrl;
    }

    /**
     * 
     * @return
     *     The authorUrl
     */
    public String getAuthorUrl() {
        return authorUrl;
    }

    /**
     * 
     * @param authorUrl
     *     The author_url
     */
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    /**
     * 
     * @return
     *     The providerName
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * 
     * @param providerName
     *     The provider_name
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

}
