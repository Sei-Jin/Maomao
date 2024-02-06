package maomao.JsonParsing.NewActivities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {
    
    @SerializedName("id")
    @Expose
    private Integer id;
    
    @SerializedName("type")
    @Expose
    private String type;
    
    @SerializedName("title")
    @Expose
    private Title title;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Title getTitle() {
        return title;
    }
    
    public void setTitle(Title title) {
        this.title = title;
    }
    
    public String getType()
    {
        return type;
    }
}