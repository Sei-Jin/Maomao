package maomao.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    
    @SerializedName("Page")
    @Expose
    private Page page;
    
    public Page getPage()
    {
        return page;
    }
    
    public void setPage(Page page)
    {
        this.page = page;
    }
}