package maomao.json_parsing.remote.latest_activity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import maomao.json_parsing.local.user_data.AniListUser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static maomao.AniListRequests.createLatestActivityTimePayload;
import static maomao.AniListRequests.sendHttpRequest;

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
        
        return new Gson().fromJson(response.body(), LatestActivityResponse.class);
    }
}