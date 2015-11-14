package prismarine.session;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prismarine.Prismarine;
import prismarine.command.Command;
import prismarine.command.CommandContext;
import prismarine.command.CommandExecutor;
import prismarine.command.CommandResult;
import prismarine.module.Module;
import prismarine.module.ModuleEvent;

import java.util.Map;
import java.util.UUID;

@Module(filename = "players.json")
public final class SessionProvider {

    private final Map<UUID, PlayerProperties> propertiesMap = Maps.newHashMap();

    private SessionProvider() {
    }

    @Subscribe
    public void onModuleLoad(ModuleEvent.Load event) {
        event.registerCommand(Command.createBuilder("version", new CommandExecutor() {
            @Override
            public CommandResult onCommandExecution(CommandContext context) throws CommandException {
                String minecraftVersion = MinecraftServer.getServer().getMinecraftVersion();
                return CommandResult.success("This server is running %s %s-%s", Prismarine.NAME, minecraftVersion,
                        Prismarine.VERSION);
            }
        }).build());
        event.registerHandler(this);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerMP && event.entity.getExtendedProperties(Prismarine.MODID) == null) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            player.registerExtendedProperties(Prismarine.MODID, getProperties(player));
        }
    }

    private PlayerProperties getProperties(EntityPlayerMP player) {
        UUID uuid = player.getGameProfile().getId();
        PlayerProperties properties = propertiesMap.get(uuid);
        if (properties == null) {
            properties = new PlayerProperties();
            propertiesMap.put(uuid, properties);
        }
        return properties;
    }

    public static PlayerProperties retrieveProperties(EntityPlayerMP player) {
        return (PlayerProperties) player.getExtendedProperties(Prismarine.MODID);
    }
}
