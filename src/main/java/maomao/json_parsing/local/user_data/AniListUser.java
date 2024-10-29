package maomao.json_parsing.local.user_data;

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
