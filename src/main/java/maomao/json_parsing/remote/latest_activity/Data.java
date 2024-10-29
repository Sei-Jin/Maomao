package maomao.json_parsing.remote.latest_activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    
    @Expose
    @SerializedName("Activity")
    private LatestActivity latestActivity;
    
    public LatestActivity getLatestActivity()
    {
        return latestActivity;
    }
}