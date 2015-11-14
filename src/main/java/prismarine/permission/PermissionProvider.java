package prismarine.permission;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import prismarine.module.Module;
import prismarine.module.ModuleEvent;
import prismarine.session.SessionProvider;

import java.util.Map;
import java.util.UUID;

@Module(filename = "permissions.json")
public final class PermissionProvider {

    @Module.Instance
    private static PermissionProvider INSTANCE = null;
    private final Map<String, GroupData> groups = Maps.newHashMap();
    private final Map<UUID, PlayerData> players = Maps.newHashMap();

    private PermissionProvider() {
    }

    @Subscribe
    public void onModuleLoad(ModuleEvent.Load event) {
        for (GroupData group : groups.values()) {
            group.initialize(null);
        }
        event.registerHandler(this);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        SessionProvider.retrieveProperties(player).initPlayerData("", player.getGameProfile().getId());
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        SessionProvider.retrieveProperties(player).releasePlayerData();
    }

    public PlayerData getPlayerData(UUID uuid) {
        PlayerData data = players.get(uuid);
        if (data == null) {
            data = new PlayerData();
            players.put(uuid, data);
        }
        return data;
    }

    public static PermissionProvider getInstance() {
        return PermissionProvider.INSTANCE;
    }

    public static boolean hasPermission(ICommandSender sender, String permission) {
        return true;
    }
}
