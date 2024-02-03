package org.example;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.Json.Activity;
import org.example.Json.JsonResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        String token = Files.readString(Path.of("token.txt"));
        
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MyListener())
                .build();
        
        // retrieveActivityData();
    }
    
    
    private static void retrieveActivityData() throws IOException, URISyntaxException, InterruptedException
    {
        AniListQuery query = new AniListQuery();
        
        List<Integer> userIDs = Files.readAllLines(Path.of("IDs.txt"))
                .stream()
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList();
        
        
        for (int userID : userIDs)
        {
            Map<String, Object> variables = new HashMap<>();
            variables.put("search", userID);
            
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("query", query.getActivityQuery());
            payload.put("variables", variables);
            
            
            Gson gson = new Gson();
            String json = gson.toJson(payload);
            
            
            // Make the HTTP Api request
            HttpClient client = HttpClient.newBuilder()
                    .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://graphql.anilist.co"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            
            // Receive the HTTP response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            
            // Parse Json Response
            JsonResponse jsonResponse = gson.fromJson(response.body(), JsonResponse.class);
            
            
            // Print out the information we want
            Activity activity = jsonResponse.getData().getActivity();
            
            System.out.println(activity.getCreatedAt());
            System.out.println(activity.getUser().getName());
            System.out.println(activity.getUser().getAvatar().getMedium());
            System.out.println(activity.getStatus());
            System.out.println(activity.getMedia().getTitle().getEnglish());
            System.out.println(activity.getMedia().getId());
            
            System.out.println();
            System.out.println();
            
            Thread.sleep(10000 / userIDs.size());
        }
    }
}