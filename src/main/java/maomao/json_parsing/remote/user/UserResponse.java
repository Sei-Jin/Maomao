package maomao.json_parsing.remote.user;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static maomao.AniListRequests.createUserDataPayload;
import static maomao.AniListRequests.sendHttpRequest;

public class UserResponse
{
    private Data data;
    
    public Data getData()
    {
        return data;
    }
    
    
    public static UserResponse getUserResponse(String username) throws URISyntaxException, IOException, InterruptedException
    {
        String userDataPayload = createUserDataPayload(username);
        
        HttpResponse<String> response = sendHttpRequest(userDataPayload);
        
        return new Gson().fromJson(response.body(), UserResponse.class);
    }
}
