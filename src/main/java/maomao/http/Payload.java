package maomao.http;

import com.google.gson.Gson;
import maomao.AniListQueries;
import maomao.json_parsing.local.user_data.AniListUser;

import java.util.HashMap;
import java.util.Map;

public class Payload
{
    public String createLatestActivityTimePayload(AniListUser user)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", user.getUserId());
        
        AniListQueries aniListQueries = new AniListQueries();
        String query = aniListQueries.getLatestActivityTime();
        
        return createPayload(query, variables);
    }
    
    
    public String createNewActivitiesPayload(AniListUser user)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", user.getUserId());
        variables.put("lastActivityTime", user.getLastActivityTime());
        
        AniListQueries aniListQueries = new AniListQueries();
        String query = aniListQueries.getNewActivities();
        
        return createPayload(query, variables);
    }
    
    
    public String createUserDataPayload(String username)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        
        AniListQueries aniListQueries = new AniListQueries();
        String query = aniListQueries.getUserData();
        
        return createPayload(query, variables);
    }
    
    
    public String createPayload(String query, Map<String, Object> variables)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query);
        payload.put("variables", variables);
        
        return new Gson().toJson(payload);
    }
}
