package maomao;

import com.google.gson.Gson;
import maomao.json_parsing.local.user_data.AniListUser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class AniListRequests
{
    public static String createLatestActivityTimePayload(AniListUser user)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", user.getUserId());
        
        AniListQueries aniListQueries = new AniListQueries();
        String query = aniListQueries.getLatestActivityTime();
        
        return createPayload(query, variables);
    }
    
    
    public static String createNewActivitiesPayload(AniListUser user)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", user.getUserId());
        variables.put("lastActivityTime", user.getLastActivityTime());
        
        AniListQueries aniListQueries = new AniListQueries();
        String query = aniListQueries.getNewActivities();
        
        return createPayload(query, variables);
    }
    
    
    public static String createUserDataPayload(String username)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        
        AniListQueries aniListQueries = new AniListQueries();
        String query = aniListQueries.getUserData();
        
        return createPayload(query, variables);
    }
    
    
    public static String createPayload(String query, Map<String, Object> variables)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query);
        payload.put("variables", variables);
        
        Gson gson = new Gson();
        return gson.toJson(payload);
    }
    
    
    public static HttpResponse<String> sendHttpRequest(String json) throws URISyntaxException, IOException, InterruptedException
    {
        HttpClient client = HttpClient.newBuilder()
                .build();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://graphql.anilist.co"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
