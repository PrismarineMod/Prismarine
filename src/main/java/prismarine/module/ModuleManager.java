package prismarine.module;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import prismarine.helper.GsonHelper;
import prismarine.helper.ReflectionHelper;

import java.util.List;

public final class ModuleManager {

    private final List<Metadata> moduleList = Lists.newArrayList();
    private final List<Object> handlerList = Lists.newArrayList();
    private final List<ICommand> commandList = Lists.newArrayList();
    private final EventBus bus = new EventBus();

    public ModuleManager() {
    }

    public void registerModule(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Module.class)) {
            moduleList.add(new Metadata(clazz));
        }
    }

    public void loadModules() {
        for (Metadata module : moduleList) {
            Optional<?> optional = GsonHelper.loadInstance(module.filename, module.clazz);
            Object instance = optional.isPresent() ? optional.get() : ReflectionHelper.newInstance(module.clazz);
            bus.register(instance);
            ReflectionHelper.setAnnotatedField(module.clazz, Module.Instance.class, instance);
            module.instance = instance;
        }
        bus.post(new ModuleEvent.Load(this));
        CommandHandler commandHandler = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        for (ICommand command : commandList) {
            commandHandler.registerCommand(command);
        }
        for (Object handler : handlerList) {
            FMLCommonHandler.instance().bus().register(handler);
            MinecraftForge.EVENT_BUS.register(handler);
        }
    }

    public void saveModules() {
        bus.post(new ModuleEvent.Save(this));
        for (Metadata module : moduleList) {
            if (module.instance != null) {
                GsonHelper.saveInstance(module.filename, module.instance);
            }
        }
    }

    public void unloadModules() {
        bus.post(new ModuleEvent.Unload(this));
        commandList.clear();
        for (Object handler : handlerList) {
            FMLCommonHandler.instance().bus().unregister(handler);
            MinecraftForge.EVENT_BUS.unregister(handler);
        }
        handlerList.clear();
        for (Metadata module : moduleList) {
            if (module.instance != null) {
                bus.unregister(module.instance);
                ReflectionHelper.setAnnotatedField(module.clazz, Module.Instance.class, null);
                module.instance = null;
            }
        }
    }

    protected List<ICommand> getCommandList() {
        return commandList;
    }

    protected List<Object> getHandlerList() {
        return handlerList;
    }

    private static final class Metadata {

        private final Class<?> clazz;
        private final String filename;
        private Object instance;

        private Metadata(Class<?> clazz) {
            this.clazz = clazz;
            this.filename = clazz.getAnnotation(Module.class).filename();
            this.instance = null;
        }
    }
}
