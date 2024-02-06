package maomao.JsonParsing.UserData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.List;

public class AniListUsers
{
    private List<AniListUser> users;
    
    public List<AniListUser> getUsers()
    {
        return users;
    }
    
    public static void updateUserData(AniListUsers aniListUsers)
    {
        try (Writer writer = new FileWriter("userData.json"))
        {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            
            gson.toJson(aniListUsers, writer);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    public static AniListUsers getUserData()
    {
        try (FileReader fileReader = new FileReader("userData.json"))
        {
            JsonReader jsonReader = new JsonReader(fileReader);
            
            Gson gson = new Gson();
            
            return gson.fromJson(jsonReader, AniListUsers.class);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
