package maomao.commands;

public enum BotCommand
{
    SET_CHANNEL("set-channel"),
    CHANGE_EMBED_COLOR("change-embed-color"),
    ADD_USER("add-user"),
    REMOVE_USER("remove-user"),
    SET_REQUEST_DELAY("set-request-delay");
    
    private final String command;
    
    BotCommand(String command)
    {
        this.command = command;
    }
    
    public String getCommand()
    {
        return command;
    }
}
