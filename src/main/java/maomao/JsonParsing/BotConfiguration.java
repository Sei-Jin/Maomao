package maomao.JsonParsing;

public class BotConfiguration
{
    private String botToken;
    private long channelId;
    private int embedColor;
    private int timeBetweenUpdateCycles;
    
    
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
    
    public int getTimeBetweenUpdateCycles()
    {
        return timeBetweenUpdateCycles;
    }
}
