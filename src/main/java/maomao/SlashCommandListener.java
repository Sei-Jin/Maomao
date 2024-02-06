package maomao;

import maomao.JsonParsing.Local.Config.BotConfiguration;
import maomao.JsonParsing.Local.UserData.AniListUser;
import maomao.JsonParsing.Local.UserData.AniListUsers;
import maomao.JsonParsing.Remote.User.UserResponse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static maomao.JsonParsing.Local.UserData.AniListUsers.updateUserData;

public class SlashCommandListener extends ListenerAdapter
{
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (event.getName().equals("set-channel"))
        {
            setChannelId(event);
        }
        else if (event.getName().equals("change-embed-color"))
        {
            changeEmbedColor(event);
        }
        else if (event.getName().equals("add-user"))
        {
            tryToAddUser(event);
        }
        else if (event.getName().equals("remove-user"))
        {
            tryToRemoveUser(event);
        }
    }
    
    private void tryToRemoveUser(SlashCommandInteractionEvent event)
    {
        try
        {
            String username = Objects.requireNonNull(event.getOption("username")).getAsString();
            
            UserResponse userResponse = UserResponse.getUserResponse(username);
            
            if (userResponse.getData() == null)
            {
                event.reply("Your request could not be processed.").queue();
                return;
            }
            else if (userResponse.getData().getUser() == null)
            {
                event.reply("Sorry, I could not find that user.").queue();
                return;
            }
            else if (AniListUsers.getUserData().getUsers() == null)    // need to test
            {
                event.reply("There are no users in the list to remove.").queue();
                return;
            }
            
            boolean userWasRemoved = removeUser(userResponse);
            
            String userLink = "["
                    + username
                    + "](<https://anilist.co/user/"
                    + username
                    + "/>)";
            
            if (userWasRemoved)
            {
                event.reply("User " + userLink + " was removed from the list of users.").queue();
            }
            else
            {
                event.reply("User " + userLink + " is not in the list of users").queue();
            }
        }
        catch (URISyntaxException | IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    private boolean removeUser(UserResponse userResponse)
    {
        AniListUsers aniListUsers = AniListUsers.getUserData();
        
        for (AniListUser user : aniListUsers.getUsers())
        {
            if (userResponse.getData().getUser().getId() == user.getUserId())
            {
                aniListUsers.getUsers().remove(user);
                
                updateUserData(aniListUsers);
                
                return true;
            }
        }
        
        return false;
    }
    
    
    private static void tryToAddUser(SlashCommandInteractionEvent event)
    {
        try
        {
            String username = Objects.requireNonNull(event.getOption("username")).getAsString();
            
            UserResponse userResponse = UserResponse.getUserResponse(username);
            
            if (userResponse.getData() == null)
            {
                event.reply("Your request could not be processed.").queue();
                return;
            }
            
            if (userResponse.getData().getUser() == null)
            {
                event.reply("Sorry, I could not find that user.").queue();
                return;
            }
            
            String userLink = "["
                    + username
                    + "](<https://anilist.co/user/"
                    + username
                    + "/>)";
            
            for (AniListUser user : AniListUsers.getUserData().getUsers())
            {
                if (userResponse.getData().getUser().getId() == user.getUserId())
                {
                    event.reply("User " + userLink + " is already added to the list.").queue();
                    return;
                }
            }

            addUser(userResponse);
            
            String replyMessage = "User "
                    + userLink
                    + " was added to the list of users.";
            
            event.reply(replyMessage).queue();
        }
        catch (URISyntaxException | IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    private static void addUser(UserResponse userResponse)
    {
        AniListUser newUser = new AniListUser();
        newUser.setUserId(userResponse.getData().getUser().getId());
        newUser.setLastActivityTime(userResponse.getData().getUser().getUpdatedAt());
        
        AniListUsers aniListUsers = AniListUsers.getUserData();
        aniListUsers.getUsers().add(newUser);
        
        updateUserData(aniListUsers);
    }
    
    
    private static void setChannelId(SlashCommandInteractionEvent event)
    {
        BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
        
        botConfiguration.setChannelId(Long.parseLong(event.getChannel().getId()));
        
        BotConfiguration.updateBotConfiguration(botConfiguration);
        
        event.reply("List update channel was set").queue();
    }
    
    
    private static void changeEmbedColor(SlashCommandInteractionEvent event)
    {
        BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
        
        botConfiguration.setEmbedColor(Objects.requireNonNull(event.getOption("embed-color")).getAsInt());
        
        BotConfiguration.updateBotConfiguration(botConfiguration);
        
        event.reply("Embed color was updated").queue();
    }
}