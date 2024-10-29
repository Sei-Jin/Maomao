package maomao.json_parsing.remote.new_activites;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import maomao.json_parsing.local.user_data.AniListUser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static maomao.AniListRequests.createNewActivitiesPayload;
import static maomao.AniListRequests.sendHttpRequest;
import static maomao.Serialization.deserialize;

public class NewActivitiesResponse
{
    
    @SerializedName("data")
    @Expose
    private Data data;
    
    public Data getData() {
        return data;
    }
    
    public void setData(Data data) {
        this.data = data;
    }
    
    public static NewActivitiesResponse getNewActivitiesResponse(AniListUser user) throws URISyntaxException, IOException, InterruptedException
    {
        String newActivitiesPayload = createNewActivitiesPayload(user);
        
        HttpResponse<String> response = sendHttpRequest(newActivitiesPayload);
        
        return deserialize(response.body(), NewActivitiesResponse.class);
    }
}