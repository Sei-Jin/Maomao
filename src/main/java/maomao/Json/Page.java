package maomao.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Page
{
    
    @SerializedName("activities")
    @Expose
    private List<Activities> activities;
    
    public List<Activities> getActivities()
    {
        return activities;
    }
    
    public void setActivities(List<Activities> activities)
    {
        this.activities = activities;
    }
}
