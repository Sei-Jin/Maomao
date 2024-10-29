package maomao.commands;

import maomao.http.Requests;
import maomao.json_parsing.local.config.BotConfiguration;
import maomao.json_parsing.local.user_data.AniListUser;
import maomao.json_parsing.local.user_data.AniListUsers;
import maomao.json_parsing.remote.user.UserObject;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class SlashCommandHandler
{
    public void setChannelId(SlashCommandInteractionEvent event)
    {
        BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
        
        botConfiguration.setChannelId(Long.parseLong(event.getChannel().getId()));
        
        BotConfiguration.updateBotConfiguration(botConfiguration);
        
        event.reply("List update channel was set").queue();
    }
    
    
    public void changeEmbedColor(SlashCommandInteractionEvent event)
    {
        BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
        
        botConfiguration.setEmbedColor(Objects.requireNonNull(event.getOption("embed-color")).getAsInt());
        
        BotConfiguration.updateBotConfiguration(botConfiguration);
        
        event.reply("Embed color was updated").queue();
    }
    
    
    public void tryToAddUser(SlashCommandInteractionEvent event)
    {
        try
        {
            String username = Objects.requireNonNull(event.getOption("username")).getAsString();
            
            UserObject userResponse = Requests.sendUserRequest(username);
            
            if (userResponse.getData() == null)
            {
                event.reply("Your request could not be processed").queue();
                return;
            }
            
            if (userResponse.getData().getUser() == null)
            {
                event.reply("I could not find an account associated with that username").queue();
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
                    event.reply("User " + userLink + " is already added to the list").queue();
                    return;
                }
            }
            
            addUser(userResponse);
            
            String replyMessage = "User "
                    + userLink
                    + " was added to the list of users";
            
            event.reply(replyMessage).queue();
        }
        catch (URISyntaxException | IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    private void addUser(UserObject userResponse)
    {
        AniListUser newUser = new AniListUser();
        newUser.setUserId(userResponse.getData().getUser().getId());
        newUser.setLastActivityTime(userResponse.getData().getUser().getUpdatedAt());
        
        AniListUsers aniListUsers = AniListUsers.getUserData();
        aniListUsers.getUsers().add(newUser);
        
        AniListUsers.updateUserData(aniListUsers);
    }
    
    
    public void tryToRemoveUser(SlashCommandInteractionEvent event)
    {
        try
        {
            String username = Objects.requireNonNull(event.getOption("username")).getAsString();
            
            UserObject userResponse = Requests.sendUserRequest(username);
            
            if (userResponse.getData() == null)
            {
                event.reply("Your request could not be processed").queue();
                return;
            }
            else if (userResponse.getData().getUser() == null)
            {
                event.reply("I could not find an account associated with that username").queue();
                return;
            }
            else if (AniListUsers.getUserData().getUsers() == null)    // need to test
            {
                event.reply("There are no users in the list to remove").queue();
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
                event.reply("User " + userLink + " was removed from the list of users").queue();
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
    
    
    private boolean removeUser(UserObject userResponse)
    {
        AniListUsers aniListUsers = AniListUsers.getUserData();
        
        for (AniListUser user : aniListUsers.getUsers())
        {
            if (userResponse.getData().getUser().getId() == user.getUserId())
            {
                aniListUsers.getUsers().remove(user);
                
                AniListUsers.updateUserData(aniListUsers);
                
                return true;
            }
        }
        
        return false;
    }
    
    
    public void setRequestDelay(SlashCommandInteractionEvent event)
    {
        BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
        
        int requestDelay = Objects.requireNonNull(event.getOption("request-delay")).getAsInt();
        
        int minimumRequestDelay = 3000;
        
        if (requestDelay < minimumRequestDelay)
        {
            event.reply("The given request delay was too low, it must be at least " + minimumRequestDelay + "ms").queue();
        }
        else
        {
            botConfiguration.setRequestDelay(requestDelay);
            
            BotConfiguration.updateBotConfiguration(botConfiguration);
            
            event.reply("The request delay was set to " + requestDelay + "ms").queue();
        }
    }
}
