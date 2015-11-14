package prismarine.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public final class CommandContext {

    private final ICommand command;
    private final ICommandSender sender;
    private final String[] arguments;

    public CommandContext(ICommand command, ICommandSender sender, String[] arguments) {
        this.command = command;
        this.sender = sender;
        this.arguments = arguments;
    }

    public ICommandSender getSender() {
        return sender;
    }

    public EntityPlayerMP getSenderAsPlayer() throws PlayerNotFoundException {
        if (sender instanceof EntityPlayerMP) {
            return (EntityPlayerMP) sender;
        }
        throw new PlayerNotFoundException();
    }

    public int getArgumentCount() {
        return arguments.length;
    }

    public String getArgument(int index) {
        return arguments[index];
    }

    public void sendToSender(String message, Object... parameters) {
        sender.addChatMessage(new ChatComponentText(String.format(message, parameters)));
    }
}
