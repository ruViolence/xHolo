package ru.violence.xholo;

import org.bukkit.plugin.java.JavaPlugin;
import ru.violence.xholo.api.VirtualEntity;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;
import ru.violence.xholo.listener.PlayerHideListener;
import ru.violence.xholo.listener.PluginDisableListener;
import ru.violence.xholo.task.VisibilityUpdateTask;

public class XHoloPlugin extends JavaPlugin {
    private static XHoloPlugin INSTANCE;
    private HologramRegistryImpl registry;

    public static XHoloPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.registry = new HologramRegistryImpl(this);

        getServer().getPluginManager().registerEvents(new PlayerHideListener(this), this);
        getServer().getPluginManager().registerEvents(new PluginDisableListener(this), this);
        new VisibilityUpdateTask(this).runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        for (VirtualEntity ve : this.registry.getAll()) {
            ve.manager().unregister();
        }

        INSTANCE = null;
    }

    public HologramRegistryImpl getRegistry() {
        return this.registry;
    }
}