package ru.violence.xholo;

import org.bukkit.plugin.java.JavaPlugin;
import ru.violence.coreapi.common.api.reflection.ReflectionUtil;
import ru.violence.xholo.api.VirtualArmorStand;
import ru.violence.xholo.api.XHolo;
import ru.violence.xholo.api.registry.HologramRegistry;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;
import ru.violence.xholo.listener.PlayerHideListener;
import ru.violence.xholo.listener.PluginDisableListener;
import ru.violence.xholo.task.VisibilityUpdateTask;

public class XHoloPlugin extends JavaPlugin {
    private HologramRegistry registry;

    @Override
    public void onEnable() {
        this.registry = new HologramRegistryImpl(this);

        ReflectionUtil.setFieldValue(XHolo.class, "registry", registry);

        getServer().getPluginManager().registerEvents(new PlayerHideListener(this), this);
        getServer().getPluginManager().registerEvents(new PluginDisableListener(this), this);
        new VisibilityUpdateTask(this).runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        for (VirtualArmorStand vas : this.registry.getAll()) {
            vas.manager().unregister();
        }

        ReflectionUtil.setFieldValue(XHolo.class, "registry", null);
    }

    public HologramRegistry getRegistry() {
        return this.registry;
    }
}