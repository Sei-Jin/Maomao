package org.example.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avatar")
    @Expose
    private Avatar avatar;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Avatar getAvatar() {
        return avatar;
    }
    
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
    
}
