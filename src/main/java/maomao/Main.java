package maomao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import maomao.JsonParsing.UserData.AniListUser;
import maomao.JsonParsing.UserData.AniListUsers;
import maomao.JsonParsing.BotConfiguration;
import maomao.JsonParsing.NewActivities.Activity;
import maomao.JsonParsing.LatestActivity.LatestActivity;
import maomao.JsonParsing.LatestActivity.LatestActivityResponse;
import maomao.JsonParsing.NewActivities.NewActivitiesResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        BotConfiguration botConfiguration = getBotConfiguration();
        
        JDA jda = JDABuilder.createLight(botConfiguration.getBotToken())
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MyListener())
                .build()
                .awaitReady();
        
        AniListUsers aniListUsers = getAniListUsers();
        
        while (true)
        {
            for (AniListUser user : aniListUsers.getUsers())
            {
                // Check if there were new list activities for the user
                String latestActivityTimePayload = createLatestActivityTimePayload(user);
                
                LatestActivity latestActivity = getLatestActivityTimeResponse(latestActivityTimePayload)
                        .getData()
                        .getLatestActivity();
                
                waitBetweenRequests(aniListUsers, botConfiguration);
                
                // If there was not a new activity, check the next user
                if (!latestActivity.getTypename().equals("ListActivity")
                        || latestActivity.getCreatedAt() <= user.getLastActivityTime())
                {
                    continue;
                }
                
                String newActivitiesPayload = createNewActivitiesPayload(user);
                
                List<Activity> activities = getNewActivitiesResponse(newActivitiesPayload)
                        .getData()
                        .getPage()
                        .getActivities();
                
                waitBetweenRequests(aniListUsers, botConfiguration);
                
                updateUserData(user, activities, aniListUsers);
                
                for (Activity activity : activities)
                {
                    if (!activity.getTypename().equals("ListActivity") || !activity.getStatus().equals("completed"))
                    {
                        continue;
                    }
                    
                    MessageEmbed embed = createMessageEmbed(activity, jda, botConfiguration);
                    
                    MessageChannel textChannel = jda.getTextChannelById(botConfiguration.getChannelId());
                    assert textChannel != null;
                    sendMessage(textChannel, embed);
                }
            }
        }
    }
    
    
    private static void waitBetweenRequests(AniListUsers aniListUsers, BotConfiguration botConfiguration) throws InterruptedException
    {
        Thread.sleep(botConfiguration.getTimeBetweenUpdateCycles() / aniListUsers.getUsers().size());
    }
    
    
    private static void updateUserData(AniListUser user, List<Activity> activities, AniListUsers aniListUsers) throws IOException
    {
        user.setLastActivityTime(activities.getLast().getCreatedAt());
        
        try (Writer writer = new FileWriter("userData.json"))
        {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            
            gson.toJson(aniListUsers, writer);
        }
    }
    
    
    private static LatestActivityResponse getLatestActivityTimeResponse(String latestActivityTimePayload) throws URISyntaxException, IOException, InterruptedException
    {
        Gson gson = new Gson();
        
        HttpResponse<String> response = sendHttpRequest(latestActivityTimePayload);
        
        return gson.fromJson(response.body(), LatestActivityResponse.class);
    }
    
    
    private static String createLatestActivityTimePayload(AniListUser user)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", user.getUserId());
        
        AniListQueries query = new AniListQueries();
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query.getLatestActivityTime());
        payload.put("variables", variables);
        
        Gson gson = new Gson();
        
        return gson.toJson(payload);
    }
    
    
    private static BotConfiguration getBotConfiguration() throws FileNotFoundException
    {
        Gson gson = new Gson();
        
        JsonReader reader = new JsonReader(new FileReader("config.json"));
        
        return gson.fromJson(reader, BotConfiguration.class);
    }
    
    
    private static AniListUsers getAniListUsers() throws FileNotFoundException
    {
        Gson gson = new Gson();
        
        JsonReader reader = new JsonReader(new FileReader("userData.json"));
        
        return gson.fromJson(reader, AniListUsers.class);
    }
    
    
    private static String createNewActivitiesPayload(AniListUser user)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", user.getUserId());
        variables.put("lastActivityTime", user.getLastActivityTime());
        
        AniListQueries query = new AniListQueries();
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query.getNewActivities());
        payload.put("variables", variables);
        
        Gson gson = new Gson();
        
        return gson.toJson(payload);
    }
    
    
    private static NewActivitiesResponse getNewActivitiesResponse(String payload) throws IOException, URISyntaxException, InterruptedException
    {
        Gson gson = new Gson();
        
        HttpResponse<String> response = sendHttpRequest(payload);
        
        return gson.fromJson(response.body(), NewActivitiesResponse.class);
    }
    
    
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
    
    
    private static MessageEmbed createMessageEmbed(Activity activity, JDA jda, BotConfiguration botConfiguration)
    {
        String userName = activity.getUser().getName();
        String titleURL = "https://anilist.co/user/" + userName + "/";
        
        String description =  activity.getStatus().substring(0, 1).toUpperCase() + activity.getStatus().substring(1)
                + " ["
                + activity.getMedia().getTitle().getEnglish()
                + "](https://anilist.co/"
                + activity.getMedia().getType().toLowerCase()
                + "/"
                + activity.getMedia().getId()
                + ")"
                + "\n _ _";
        
        String imageURL = "https://img.anili.st/media/" + activity.getMedia().getId();
        
        return new EmbedBuilder()
                .setColor(botConfiguration.getEmbedColor())                         // 2829634 is the AniList embed color
                .setAuthor(jda.getSelfUser().getName())
                .setTitle(userName, titleURL)
                .setDescription(description)
                .setThumbnail(activity.getUser().getAvatar().getMedium())
                .setImage(imageURL)
                .build();
    }
    
    
    private static void sendMessage(MessageChannel channel, MessageEmbed embed)
    {
        channel.sendMessageEmbeds(embed).queue();
    }
}