package maomao.JsonParsing.Remote.NewActivities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Page
{
    
    @SerializedName("activities")
    @Expose
    private List<Activity> activities;
    
    public List<Activity> getActivities()
    {
        return activities;
    }
    
    public void setActivities(List<Activity> activities)
    {
        this.activities = activities;
    }
}
