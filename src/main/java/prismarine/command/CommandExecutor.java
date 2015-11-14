package prismarine.command;

import net.minecraft.command.CommandException;

public interface CommandExecutor {

    CommandResult onCommandExecution(CommandContext context) throws CommandException;
}
