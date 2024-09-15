package ru.violence.xholo.task;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.VirtualEntity;
import ru.violence.xholo.api.impl.ManagerImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisibilityUpdateTask extends BukkitRunnable {
    private final XHoloPlugin plugin;

    public VisibilityUpdateTask(XHoloPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Map<World, List<Player>> worldPlayersCache = new HashMap<>();
        for (VirtualEntity ve : plugin.getRegistry().getAll()) {
            ManagerImpl manager = (ManagerImpl) ve.manager();
            manager.updateVisibility(worldPlayersCache, null);
        }
    }
}
