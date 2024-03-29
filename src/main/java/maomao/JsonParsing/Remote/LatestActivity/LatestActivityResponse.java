package maomao.JsonParsing.Remote.LatestActivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import maomao.JsonParsing.Local.UserData.AniListUser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static maomao.AniListRequests.createLatestActivityTimePayload;
import static maomao.AniListRequests.sendHttpRequest;
import static maomao.Serialization.deserialize;

public class LatestActivityResponse
{
    @SerializedName("data")
    @Expose
    private Data data;
    
    public Data getData() {
        return data;
    }
    
    public static LatestActivityResponse getLatestActivityResponse(AniListUser user) throws URISyntaxException, IOException, InterruptedException
    {
        String latestActivityTimePayload = createLatestActivityTimePayload(user);
        
        HttpResponse<String> response = sendHttpRequest(latestActivityTimePayload);
        
        return deserialize(response.body(), LatestActivityResponse.class);
    }
}