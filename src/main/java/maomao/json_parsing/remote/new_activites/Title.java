package maomao.json_parsing.remote.new_activites;

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