package prismarine.session;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import prismarine.permission.PermissionProvider;
import prismarine.permission.PlayerData;

import java.util.UUID;

public final class PlayerProperties implements IExtendedEntityProperties {

    private transient PlayerData playerData = null;

    public PlayerProperties() {
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public void initPlayerData(String worldName, UUID uuid) {
        playerData = PermissionProvider.getInstance().getPlayerData(uuid);
        playerData.initialize(worldName);
    }

    public void releasePlayerData() {
        playerData.release();
        playerData = null;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }
}
