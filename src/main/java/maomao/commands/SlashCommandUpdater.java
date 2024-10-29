package maomao.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SlashCommandUpdater
{
    public static void updateSlashCommands(JDA jda)
    {
        jda.getGuilds()
                .getFirst()
                .updateCommands()
                .addCommands(
                        Commands.slash(BotCommand.SET_CHANNEL.getCommand(), "Set the channel that will receive list updates")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        
                        Commands.slash(BotCommand.ADD_USER.getCommand(), "Add a user to the list of users")
                                .addOption(OptionType.STRING, "username", "An AniList username")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        
                        Commands.slash(BotCommand.REMOVE_USER.getCommand(), "Remove a user from the list of users")
                                .addOption(OptionType.STRING, "username", "An AniList username")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        
                        Commands.slash(BotCommand.CHANGE_EMBED_COLOR.getCommand(), "Changes the color of the embeds")
                                .addOption(OptionType.INTEGER, "embed-color", "The color of the embed as an integer value")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                        
                        
                        Commands.slash(BotCommand.SET_REQUEST_DELAY.getCommand(), "Changes the delay between requests")
                                .addOption(OptionType.INTEGER, "request-delay", "The wait time between requests in milliseconds")
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                ).queue();
    }
}
