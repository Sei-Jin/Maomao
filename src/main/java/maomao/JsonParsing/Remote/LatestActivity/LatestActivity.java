package maomao.JsonParsing.Remote.LatestActivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestActivity
{
    @SerializedName("__typename")
    @Expose
    private String typename;
    
    @SerializedName("createdAt")
    @Expose
    private Integer createdAt;
    
    public Integer getCreatedAt()
    {
        return createdAt;
    }
    
    public String getTypename()
    {
        return typename;
    }
}
