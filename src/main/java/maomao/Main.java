package maomao;

import maomao.JsonParsing.LatestActivity.LatestActivityResponse;
import maomao.JsonParsing.NewActivities.NewActivitiesResponse;
import maomao.JsonParsing.UserData.AniListUser;
import maomao.JsonParsing.UserData.AniListUsers;
import maomao.JsonParsing.BotConfiguration;
import maomao.JsonParsing.NewActivities.Activity;
import maomao.JsonParsing.LatestActivity.LatestActivity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.*;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

import static maomao.AniListRequests.*;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
        
        JDA jda = JDABuilder.createLight(botConfiguration.getBotToken())
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MyListener())
                .build()
                .awaitReady();
        
        updateSlashCommands(jda);
        
        AniListUsers aniListUsers = AniListUsers.getUserData();
        
        while (true)
        {
            for (AniListUser user : aniListUsers.getUsers())
            {
                // Check if there were new list activities for the user
                LatestActivity latestActivity = getLatestActivity(user);
                
                waitBetweenRequests(aniListUsers, botConfiguration);
                
                // If there was not a new activity, check the next user
                if (!latestActivity.getTypename().equals("ListActivity")
                        || latestActivity.getCreatedAt() <= user.getLastActivityTime())
                {
                    continue;
                }
                
                List<Activity> activities = getActivities(user);
                
                waitBetweenRequests(aniListUsers, botConfiguration);
                
                user.setLastActivityTime(activities.getLast().getCreatedAt());
                
                AniListUsers.updateUserData(aniListUsers);
                
                for (Activity activity : activities)
                {
                    if (!activity.getTypename().equals("ListActivity") || !activity.getStatus().equals("completed"))
                    {
                        continue;
                    }
                    
                    MessageEmbed embed = createMessageEmbed(activity, jda, botConfiguration);
                    
                    MessageChannel textChannel = jda.getTextChannelById(botConfiguration.getChannelId());
                    
                    sendMessage(Objects.requireNonNull(textChannel), embed);
                }
            }
        }
    }
    
    
    private static List<Activity> getActivities(AniListUser user) throws URISyntaxException, IOException, InterruptedException
    {
        String newActivitiesPayload = createNewActivitiesPayload(user);
        
        HttpResponse<String> response = sendHttpRequest(newActivitiesPayload);
        
        return deserialize(response.body(), NewActivitiesResponse.class)
                .getData()
                .getPage()
                .getActivities();
    }
    
    
    private static LatestActivity getLatestActivity(AniListUser user) throws URISyntaxException, IOException, InterruptedException
    {
        String latestActivityTimePayload = createLatestActivityTimePayload(user);
        
        HttpResponse<String> response = sendHttpRequest(latestActivityTimePayload);
        
        return deserialize(response.body(), LatestActivityResponse.class)
                .getData()
                .getLatestActivity();
    }
    
    
    private static void updateSlashCommands(JDA jda)
    {
        for (Guild guild : jda.getGuilds())
        {
            guild.updateCommands().
                    addCommands(
                        Commands.slash("set-channel", "Set the channel that will receive list updates")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        Commands.slash("add-user", "Add a user to the list of users")
                            .addOption(OptionType.STRING, "username", "An AniList username")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        Commands.slash("remove-user", "Remove a user from the list of users")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        Commands.slash("change-embed-color", "Changes the color of the embeds")
                            .addOption(OptionType.INTEGER, "embed-color", "The color of the embed as an integer value")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                    ).queue();
        }
    }
    
    
    private static void waitBetweenRequests(AniListUsers aniListUsers, BotConfiguration botConfiguration) throws InterruptedException
    {
        Thread.sleep(botConfiguration.getTimeBetweenUpdateCycles() / aniListUsers.getUsers().size());
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