package prismarine;

import com.google.common.io.Files;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import prismarine.module.ModuleManager;
import prismarine.permission.PermissionProvider;
import prismarine.session.SessionProvider;

import java.io.File;
import java.io.IOException;

@Mod(modid = Prismarine.MODID, name = Prismarine.NAME, version = Prismarine.VERSION, acceptableRemoteVersions = "*")
public enum Prismarine {
    INSTANCE;

    public static final String MODID = "prismarine";
    public static final String NAME = "Prismarine";
    public static final String VERSION = "git";
    private final ModuleManager moduleManager = new ModuleManager();
    private Logger logger = null;

    Prismarine() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onPreInitialization(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        moduleManager.registerModule(SessionProvider.class);
        moduleManager.registerModule(PermissionProvider.class);
    }

    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        moduleManager.loadModules();
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        moduleManager.unloadModules();
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        if (!event.world.isRemote && event.world.provider.getDimensionId() == 0) {
            moduleManager.saveModules();
        }
    }

    public static File getFile(String filename) {
        File file = new File(Prismarine.MODID, filename);
        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
            } catch (IOException e) {
                Prismarine.getLogger().catching(e);
            }
        }
        return file;
    }

    @Mod.InstanceFactory
    public static Prismarine getInstance() {
        return Prismarine.INSTANCE;
    }

    public static Logger getLogger() {
        return Prismarine.getInstance().logger;
    }
}
