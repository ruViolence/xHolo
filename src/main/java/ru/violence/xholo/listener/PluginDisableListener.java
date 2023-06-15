package ru.violence.xholo.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.VirtualArmorStand;

public class PluginDisableListener implements Listener {
    private final XHoloPlugin plugin;

    public PluginDisableListener(XHoloPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();

        for (VirtualArmorStand vas : this.plugin.getRegistry().getAll()) {
            if (plugin.equals(vas.getPlugin())) {
                vas.manager().setAutoUpdate(true);
                vas.manager().unregister();
            }
        }
    }
}
