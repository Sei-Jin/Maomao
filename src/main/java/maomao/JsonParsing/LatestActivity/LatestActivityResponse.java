package maomao.JsonParsing.LatestActivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestActivityResponse
{
    @SerializedName("data")
    @Expose
    private Data data;
    
    public Data getData() {
        return data;
    }
}