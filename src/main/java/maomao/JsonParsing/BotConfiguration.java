package maomao.JsonParsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;

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
    
    
    public static BotConfiguration getBotConfiguration()
    {
        try (FileReader fileReader = new FileReader("config.json"))
        {
            JsonReader jsonReader = new JsonReader(fileReader);
            
            Gson gson = new Gson();
            
            return gson.fromJson(jsonReader, BotConfiguration.class);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    public static void updateBotConfiguration(BotConfiguration botConfiguration)
    {
        try (FileWriter fileWriter = new FileWriter("config.json"))
        {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            
            gson.toJson(botConfiguration, fileWriter);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
