package maomao;

import maomao.JsonParsing.Remote.LatestActivity.LatestActivityResponse;
import maomao.JsonParsing.Remote.NewActivities.NewActivitiesResponse;
import maomao.JsonParsing.Local.UserData.AniListUser;
import maomao.JsonParsing.Local.UserData.AniListUsers;
import maomao.JsonParsing.Local.Config.BotConfiguration;
import maomao.JsonParsing.Remote.NewActivities.Activity;
import maomao.JsonParsing.Remote.LatestActivity.LatestActivity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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
        
        updateSlashCommands(jda);
        
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
                LatestActivityResponse latestActivityResponse = LatestActivityResponse.getLatestActivityResponse(user);
                
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
                
                NewActivitiesResponse newActivitiesResponse = NewActivitiesResponse.getNewActivitiesResponse(user);
                
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
                    
                    sendMessage(Objects.requireNonNull(listUpdateChannel), embed);
                }
            }
        }
    }
    
    
    private static void waitBetweenRequests(BotConfiguration botConfiguration) throws InterruptedException
    {
        Thread.sleep(botConfiguration.getRequestDelay());
    }
    
    
    private static void updateSlashCommands(JDA jda)
    {
        jda.getGuilds().getFirst().updateCommands()
                .addCommands(
                        Commands.slash("set-channel", "Set the channel that will receive list updates")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        Commands.slash("add-user", "Add a user to the list of users")
                                .addOption(OptionType.STRING, "username", "An AniList username")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        Commands.slash("remove-user", "Remove a user from the list of users")
                                .addOption(OptionType.STRING, "username", "An AniList username")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        Commands.slash("change-embed-color", "Changes the color of the embeds")
                                .addOption(OptionType.INTEGER, "embed-color", "The color of the embed as an integer value")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        Commands.slash("set-request-delay", "Changes the delay between requests")
                                .addOption(OptionType.INTEGER, "request-delay", "The wait time between requests in milliseconds")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                ).queue();
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
    
    
    private static void sendMessage(MessageChannel channel, MessageEmbed embed)
    {
        channel.sendMessageEmbeds(embed).queue();
    }
}