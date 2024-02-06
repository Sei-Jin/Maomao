package maomao.JsonParsing.Remote.User;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Objects;

import static maomao.AniListRequests.createUserDataPayload;
import static maomao.AniListRequests.sendHttpRequest;
import static maomao.Serialization.deserialize;

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
        
        return deserialize(response.body(), UserResponse.class);
    }
}
