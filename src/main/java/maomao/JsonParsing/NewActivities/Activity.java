package maomao.JsonParsing.NewActivities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity
{
    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("createdAt")
    @Expose
    private Integer createdAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("media")
    @Expose
    private Media media;
    @SerializedName("user")
    @Expose
    private User user;
    
    public String getTypename() {
        return typename;
    }
    
    public void setTypename(String typename) {
        this.typename = typename;
    }
    
    public Integer getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Media getMedia() {
        return media;
    }
    
    public void setMedia(Media media) {
        this.media = media;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
}