package maomao;

import com.google.gson.Gson;

public class Serialization
{
    static Gson gson = new Gson();
    
    public static <T> T deserialize(String json, Class<T> thisClass)
    {
        return gson.fromJson(json, thisClass);
    }
    
    public static String serialize(Object object)
    {
        return gson.toJson(object);
    }
}
