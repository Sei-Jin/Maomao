package maomao.http;

import com.google.gson.Gson;
import maomao.json_parsing.local.user_data.AniListUser;
import maomao.json_parsing.remote.latest_activity.LatestActivityObject;
import maomao.json_parsing.remote.new_activites.NewActivitiesObject;
import maomao.json_parsing.remote.user.UserObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Requests
{
    private static HttpResponse<String> sendHttpRequest(String json) throws URISyntaxException, IOException, InterruptedException
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
    
    
    public static LatestActivityObject sendLatestActivityRequest(AniListUser user) throws URISyntaxException, IOException, InterruptedException
    {
        String latestActivityTimePayload = new Payload().createLatestActivityTimePayload(user);
        HttpResponse<String> response = sendHttpRequest(latestActivityTimePayload);
        
        return new Gson().fromJson(response.body(), LatestActivityObject.class);
    }
    
    
    public static NewActivitiesObject sendNewActivitiesRequest(AniListUser user) throws URISyntaxException, IOException, InterruptedException
    {
        String newActivitiesPayload = new Payload().createNewActivitiesPayload(user);
        HttpResponse<String> response = sendHttpRequest(newActivitiesPayload);
        
        return new Gson().fromJson(response.body(), NewActivitiesObject.class);
    }
    
    
    public static UserObject sendUserRequest(String username) throws URISyntaxException, IOException, InterruptedException
    {
        String userDataPayload = new Payload().createUserDataPayload(username);
        HttpResponse<String> response = sendHttpRequest(userDataPayload);
        
        return new Gson().fromJson(response.body(), UserObject.class);
    }
}
