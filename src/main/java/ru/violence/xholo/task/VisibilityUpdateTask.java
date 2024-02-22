package ru.violence.xholo.task;

import org.bukkit.scheduler.BukkitRunnable;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.VirtualEntity;
import ru.violence.xholo.api.impl.ManagerImpl;

public class VisibilityUpdateTask extends BukkitRunnable {
    private final XHoloPlugin plugin;

    public VisibilityUpdateTask(XHoloPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (VirtualEntity ve : plugin.getRegistry().getAll()) {
            ManagerImpl manager = (ManagerImpl) ve.manager();
            manager.updateVisibility();
        }
    }
}
