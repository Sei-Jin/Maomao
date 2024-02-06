package maomao.JsonParsing.UserData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import maomao.JsonParsing.NewActivities.Activity;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class AniListUser
{
    private int userId;
    private int lastActivityTime;
    
    public int getUserId()
    {
        return userId;
    }
    
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    
    public int getLastActivityTime()
    {
        return lastActivityTime;
    }
    
    public void setLastActivityTime(int lastActivityTime)
    {
        this.lastActivityTime = lastActivityTime;
    }
}
