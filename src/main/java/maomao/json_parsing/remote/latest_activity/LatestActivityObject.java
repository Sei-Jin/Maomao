package maomao.json_parsing.remote.latest_activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestActivityObject
{
    @SerializedName("data")
    @Expose
    private Data data;
    
    public Data getData() {
        return data;
    }
}