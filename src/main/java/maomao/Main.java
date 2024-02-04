package maomao;

import com.google.gson.Gson;
import maomao.Json.JsonResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import maomao.Json.Activity;

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
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        String token = Files.readString(Path.of("token.txt"));
        
        JDA jda = JDABuilder.createLight(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MyListener())
                .build()
                .awaitReady();
        
        MessageChannel textChannel = jda.getTextChannelById("474760408251498498");
        
        List<Integer> userIDs = Files.readAllLines(Path.of("IDs.txt"))
                .stream()
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList();
        
        AniListQuery query = new AniListQuery();
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("search", userIDs.getFirst());
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query.getActivityQuery());
        payload.put("variables", variables);
        
        
        Activity activity = getJsonResponse(payload).getData().getActivity();
        
        MessageEmbed embed = createMessageEmbed(activity, jda);
        
        assert textChannel != null;
        sendMessage(textChannel, embed);
    }
    
    
    private static MessageEmbed createMessageEmbed(Activity activity, JDA jda)
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
                .setColor(3066993)                         // 2829634 is the AniList embed color
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
    
    
    private static JsonResponse getJsonResponse(Map<String, Object> payload) throws IOException, URISyntaxException, InterruptedException
    {
        Gson gson = new Gson();
        
        String json = gson.toJson(payload);
        
        HttpResponse<String> response = sendHttpRequest(json);
        
        return gson.fromJson(response.body(), JsonResponse.class);
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
}