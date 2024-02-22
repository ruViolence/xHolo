package ru.violence.xholo.listener;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.VirtualEntity;

import java.util.List;

public class PlayerHideListener implements Listener {
    private final XHoloPlugin plugin;

    public PlayerHideListener(XHoloPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent event) {
        hideAll(event.getPlayer(), event.getFrom());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        hideAll(event.getPlayer(), null);
    }

    private void hideAll(@NotNull Player player, @Nullable World world) {
        List<VirtualEntity> virtualEntities = world == null
                ? plugin.getRegistry().getAll()
                : plugin.getRegistry().getAllFrom(world);

        for (VirtualEntity ve : virtualEntities) {
            ve.manager().hide(player);
        }
    }
}
