package maomao.LatestActivityJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestActivity
{
    @SerializedName("createdAt")
    @Expose
    private Integer createdAt;
    
    public Integer getCreatedAt()
    {
        return createdAt;
    }
}
