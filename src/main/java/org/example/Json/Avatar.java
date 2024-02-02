package org.example.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Avatar {
    
    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("medium")
    @Expose
    private String medium;
    
    public String getLarge() {
        return large;
    }
    
    public void setLarge(String large) {
        this.large = large;
    }
    
    public String getMedium() {
        return medium;
    }
    
    public void setMedium(String medium) {
        this.medium = medium;
    }
    
}