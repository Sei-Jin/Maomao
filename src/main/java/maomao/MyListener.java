package maomao;

import maomao.JsonParsing.BotConfiguration;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

import static maomao.AniListRequests.createUserInfoPayload;

public class MyListener extends ListenerAdapter
{
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (event.getName().equals("set-channel"))
        {
            BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
            
            botConfiguration.setChannelId(Long.parseLong(event.getChannel().getId()));
            
            BotConfiguration.updateBotConfiguration(botConfiguration);
            
            event.reply("List update channel was set").queue();
        }
        else if (event.getName().equals("change-embed-color"))
        {
            BotConfiguration botConfiguration = BotConfiguration.getBotConfiguration();
            
            botConfiguration.setEmbedColor(Objects.requireNonNull(event.getOption("embed-color")).getAsInt());
            
            BotConfiguration.updateBotConfiguration(botConfiguration);
            
            event.reply("Embed color was updated").queue();
        }
        else if (event.getName().equals("add-user"))
        {
            // search username to get id
            String searchUserPayload = createUserInfoPayload(Objects.requireNonNull(event.getOption("username")).getAsString());
            
            // add id to the list of users if the username is valid
            
            
            
        }
    }
}