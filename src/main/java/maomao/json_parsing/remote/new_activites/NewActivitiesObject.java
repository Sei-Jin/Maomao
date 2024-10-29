package maomao.json_parsing.remote.new_activites;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewActivitiesObject
{
    @SerializedName("data")
    @Expose
    private Data data;
    
    public Data getData() {
        return data;
    }
    
    public void setData(Data data) {
        this.data = data;
    }
}