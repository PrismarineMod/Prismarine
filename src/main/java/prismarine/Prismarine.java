package prismarine;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = Prismarine.MODID, name = Prismarine.NAME, version = Prismarine.VERSION, acceptableRemoteVersions = "*")
public enum Prismarine {
    INSTANCE;

    public static final String MODID = "prismarine";
    public static final String NAME = "Prismarine";
    public static final String VERSION = "git";

    Prismarine() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.InstanceFactory
    public static Prismarine getInstance() {
        return Prismarine.INSTANCE;
    }
}
