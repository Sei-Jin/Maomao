package maomao.JsonParsing.NewActivities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Title {
    
    @SerializedName("english")
    @Expose
    private String english;
    
    public String getEnglish() {
        return english;
    }
    
    public void setEnglish(String english) {
        this.english = english;
    }
}