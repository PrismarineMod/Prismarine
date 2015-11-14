package prismarine.command;

import net.minecraft.command.CommandException;

public final class CommandResult {

    private static final CommandResult NONE = new CommandResult(null, null, true);
    private final String message;
    private final Object[] parameters;
    private final boolean successful;

    private CommandResult(String message, Object[] parameters, boolean successful) {
        this.message = message;
        this.parameters = parameters;
        this.successful = successful;
    }

    public void evaluate(CommandContext context) throws CommandException {
        if (this != CommandResult.NONE) {
            if (successful) {
                context.sendToSender(message, parameters);
            } else {
                throw new CommandException(String.format(message, parameters));
            }
        }
    }

    public static CommandResult failure(String message, Object... parameters) {
        return new CommandResult(message, parameters, false);
    }

    public static CommandResult none() {
        return CommandResult.NONE;
    }

    public static CommandResult success(String message, Object... parameters) {
        return new CommandResult(message, parameters, true);
    }
}
