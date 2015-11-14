package prismarine.command;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import prismarine.permission.PermissionProvider;

import java.util.Collections;
import java.util.List;

public final class Command implements ICommand {

    private final String name;
    private final String usage;
    private final List<String> aliasList;
    private final CommandExecutor executor;
    private final String permission;

    private Command(Builder builder) {
        this.name = builder.name;
        this.usage = builder.usage;
        this.aliasList = ImmutableList.copyOf(builder.aliases);
        this.executor = builder.executor;
        this.permission = builder.permission;
    }

    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return usage;
    }

    @Override
    public List<String> getCommandAliases() {
        return aliasList;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) throws CommandException {
        CommandContext context = new CommandContext(this, sender, arguments);
        executor.onCommandExecution(context).evaluate(context);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return PermissionProvider.hasPermission(sender, permission);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] arguments, BlockPos pos) {
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] arguments, int index) {
        return false;
    }

    @Override
    public int compareTo(Object other) {
        return name.compareTo(((ICommand) other).getCommandName());
    }

    public static Builder createBuilder(String name, CommandExecutor executor) {
        return new Builder(name, executor);
    }

    public static final class Builder {

        private final String name;
        private final CommandExecutor executor;
        private String usage;
        private String[] aliases;
        private String permission;

        private Builder(String name, CommandExecutor executor) {
            this.name = name;
            this.executor = executor;
            this.usage = "";
            this.aliases = new String[0];
            this.permission = null;
        }

        public Builder setUsage(String usage) {
            this.usage = usage;
            return this;
        }

        public Builder setAliases(String... aliases) {
            this.aliases = aliases;
            return this;
        }

        public Builder setPermission(String permission) {
            this.permission = permission;
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }
}
