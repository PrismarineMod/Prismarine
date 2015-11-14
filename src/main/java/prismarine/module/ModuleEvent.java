package prismarine.module;

import net.minecraft.command.ICommand;

public abstract class ModuleEvent {

    protected final ModuleManager moduleManager;

    protected ModuleEvent(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public static final class Load extends ModuleEvent {

        protected Load(ModuleManager moduleManager) {
            super(moduleManager);
        }

        public void registerCommand(ICommand command) {
            moduleManager.getCommandList().add(command);
        }

        public void registerHandler(Object handler) {
            moduleManager.getHandlerList().add(handler);
        }
    }

    public static final class Save extends ModuleEvent {

        protected Save(ModuleManager moduleManager) {
            super(moduleManager);
        }
    }

    public static final class Unload extends ModuleEvent {

        protected Unload(ModuleManager moduleManager) {
            super(moduleManager);
        }
    }
}
