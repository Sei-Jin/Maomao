package maomao;

import maomao.commands.SlashCommandListener;
import maomao.commands.SlashCommandUpdater;
import maomao.http.Request;
import maomao.json_parsing.remote.latest_activity.LatestActivityObject;
import maomao.json_parsing.remote.new_activites.NewActivitiesObject;
import maomao.json_parsing.local.user_data.AniListUser;
import maomao.json_parsing.local.user_data.AniListUsers;
import maomao.json_parsing.local.config.BotConfiguration;
import maomao.json_parsing.remote.new_activites.Activity;
import maomao.json_parsing.remote.latest_activity.LatestActivity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;


public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
        
        JDA jda = JDABuilder.createLight(botConfiguration.getBotToken())
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new SlashCommandListener())
                .build()
                .awaitReady();
        
        SlashCommandUpdater.updateSlashCommands(jda);
        
        checkListActivitiesLoop(jda);
    }
    
    
    private static void checkListActivitiesLoop(JDA jda) throws URISyntaxException, IOException, InterruptedException
    {
        AniListUsers aniListUsers;
        BotConfiguration botConfiguration;
        
        while (true)
        {
            aniListUsers = AniListUsers.getUserData();
            botConfiguration = BotConfiguration.getBotConfiguration();
            
            if (aniListUsers == null)
            {
                waitBetweenRequests(botConfiguration);
                
                continue;
            }
            
            for (AniListUser user : aniListUsers.getUsers())
            {
                // Check if there were new list activities for the user
                LatestActivityObject latestActivityResponse = Request.sendLatestActivityRequest(user);
                
                waitBetweenRequests(botConfiguration);
                
                if (latestActivityResponse.getData() == null)
                {
                    continue;
                }
                
                LatestActivity latestActivity = latestActivityResponse.getData().getLatestActivity();
                
                // If there was not a new activity, check the next user
                if (!latestActivity.getTypename().equals("ListActivity")
                        || latestActivity.getCreatedAt() <= user.getLastActivityTime())
                {
                    continue;
                }
                
                NewActivitiesObject newActivitiesResponse = Request.sendNewActivitiesRequest(user);
                
                waitBetweenRequests(botConfiguration);
                
                List<Activity> activities = newActivitiesResponse.getData().getPage().getActivities();
                
                user.setLastActivityTime(activities.getLast().getCreatedAt());
                AniListUsers.updateUserData(aniListUsers);
                
                for (Activity activity : activities)
                {
                    if (!activity.getTypename().equals("ListActivity") || !activity.getStatus().equals("completed"))
                    {
                        continue;
                    }
                    
                    MessageEmbed embed = createMessageEmbed(activity, jda, botConfiguration);
                    
                    MessageChannel listUpdateChannel = jda.getTextChannelById(botConfiguration.getChannelId());
                    
                    Objects.requireNonNull(listUpdateChannel).sendMessageEmbeds(embed).queue();
                }
            }
        }
    }
    
    
    private static void waitBetweenRequests(BotConfiguration botConfiguration) throws InterruptedException
    {
        Thread.sleep(botConfiguration.getRequestDelay());
    }
    
    
    private static MessageEmbed createMessageEmbed(Activity activity, JDA jda, BotConfiguration botConfiguration)
    {
        String userName = activity.getUser().getName();
        String titleURL = "https://anilist.co/user/" + userName + "/";
        
        String description = activity.getStatus().substring(0, 1).toUpperCase() + activity.getStatus().substring(1)
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
                .setColor(botConfiguration.getEmbedColor())
                .setAuthor(jda.getSelfUser().getName())
                .setTitle(userName, titleURL)
                .setDescription(description)
                .setThumbnail(activity.getUser().getAvatar().getMedium())
                .setImage(imageURL)
                .build();
    }
}