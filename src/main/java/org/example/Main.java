package org.example;

import com.google.gson.Gson;
import org.example.Json.Activity;
import org.example.Json.JsonResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Main
{
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException
    {
        // Here we define our query as a multi-line string
        String query = """
        query ($search: Int) {
            Activity (userId: $search, sort: ID_DESC) {
                __typename
                ... on ListActivity {
                    createdAt
                    status
                    media {
                        id
                        title {
                            english
                        }
                    }
                    user {
                        name
                        avatar {
                            large
                            medium
                        }
                    }
                }
            }
        }
        """;
        query = query.replace('\n', ' ');
        
        
        // Define our query variables and values that will be used in the query request
        int userID = 0;
        
        Map<String, Object> variables = new HashMap<>();
        
        variables.put("search", userID);
        
        
        Map<String, Object> payload = new HashMap<>();
        
        payload.put("query", query);
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

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println(response.body());
        
        JsonResponse jsonResponse = gson.fromJson(response.body(), JsonResponse.class);
        
        Activity activity = jsonResponse.getData().getActivity();
        
        System.out.println(activity.getCreatedAt());
        System.out.println(activity.getUser().getName());
        System.out.println(activity.getUser().getAvatar().getMedium());
        System.out.println(activity.getStatus());
        System.out.println(activity.getMedia().getTitle().getEnglish());
        System.out.println(activity.getMedia().getId());
    }
}