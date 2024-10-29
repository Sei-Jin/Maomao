package maomao.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter
{
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        BotCommand commandName;
        
        try
        {
            commandName = BotCommand.valueOf(event.getName().toUpperCase().replace("-", "_"));
        }
        catch (IllegalArgumentException e)
        {
            // Handle unknown command
            event.reply("Unknown command: " + event.getName()).setEphemeral(true).queue();
            return;
        }
        
        SlashCommandHandler slashCommandHandler = new SlashCommandHandler();
        
        switch (commandName)
        {
            case SET_CHANNEL -> slashCommandHandler.setChannelId(event);
            case CHANGE_EMBED_COLOR -> slashCommandHandler.changeEmbedColor(event);
            case ADD_USER -> slashCommandHandler.tryToAddUser(event);
            case REMOVE_USER -> slashCommandHandler.tryToRemoveUser(event);
            case SET_REQUEST_DELAY -> slashCommandHandler.setRequestDelay(event);
        }
    }
}