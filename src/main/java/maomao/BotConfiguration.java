package maomao;

public class BotConfiguration
{
    private String botToken;
    private long channelId;
    private int embedColor;
    
    
    public String getBotToken()
    {
        return botToken;
    }
    
    public void setBotToken(String botToken)
    {
        this.botToken = botToken;
    }
    
    public long getChannelId()
    {
        return channelId;
    }
    
    public void setChannelId(long channelId)
    {
        this.channelId = channelId;
    }
    
    public int getEmbedColor()
    {
        return embedColor;
    }
    
    public void setEmbedColor(int embedColor)
    {
        this.embedColor = embedColor;
    }
}
