package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.Manager;
import ru.violence.xholo.util.UpdateFlag;
import ru.violence.xholo.util.Utils;
import ru.violence.xholo.util.nms.NMSUtil;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public final class ManagerImpl implements Manager {
    private final VirtualArmorStandImpl vas;
    private final @NotNull Set<Player> viewers = new HashSet<>();
    private int displayRange = DEFAULT_DISPLAY_RANGE;
    private @Nullable Predicate<Player> canSeeFilter;
    private boolean registered;
    private boolean autoUpdate = true;

    public ManagerImpl(VirtualArmorStandImpl vas) {
        this.vas = vas;
    }

    @Override
    public boolean isShown(@NotNull Player player) {
        Check.notNull(player, "Player is null");
        synchronized (vas) {
            return viewers.contains(player);
        }
    }

    @Override
    public boolean show(@NotNull Player player) {
        synchronized (vas) {
            if (isShown(player)) return false;

            viewers.add(player);

            int entityId = vas.getEntityId();

            NMSUtil.spawnEntityArmorStand(player, entityId, vas.getLocation(), vas.getData(),
                    vas.getItemInHand(),
                    vas.getItemInOffHand(),
                    vas.getHelmet(),
                    vas.getChestplate(),
                    vas.getLeggings(),
                    vas.getBoots());

            return true;
        }
    }

    @Override
    public boolean hide(@NotNull Player player) {
        return hide(player, true);
    }

    public boolean hide(@NotNull Player player, boolean sendPacket) {
        synchronized (vas) {
            if (!isShown(player)) return false;

            viewers.remove(player);
            if (sendPacket) NMSUtil.destroyEntities(player, vas.getEntityId());

            return true;
        }
    }

    public void hideAll() {
        synchronized (vas) {
            for (Player player : viewers) {
                NMSUtil.destroyEntities(player, vas.getEntityId());
            }

            viewers.clear();
        }
    }

    @Override
    public @Nullable Predicate<Player> getCanSeeFilter() {
        return canSeeFilter;
    }

    @Override
    public void setCanSeeFilter(@Nullable Predicate<Player> filter) {
        this.canSeeFilter = filter;
    }

    @Override
    public int getDisplayRange() {
        return displayRange;
    }

    @Override
    public void setDisplayRange(@Range(from = 1, to = Integer.MAX_VALUE) int displayRange) {
        this.displayRange = Check.moreThan(displayRange, 0, "Display range is 0 or less");
        updateVisibility();
    }

    @Override
    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    @Override
    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    @Override
    public int getViewersAmount() {
        synchronized (vas) {
            return viewers.size();
        }
    }

    @Override
    public @NotNull Set<Player> getViewers() {
        synchronized (vas) {
            return new HashSet<>(viewers);
        }
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    @Override
    public void register() {
        synchronized (vas) {
            if (registered) return;
            vas.getRegistry().register(vas);
            registered = true;

            updateVisibility();
        }
    }

    @Override
    public void unregister() {
        synchronized (vas) {
            if (!registered) return;
            vas.getRegistry().unregister(vas);
            registered = false;

            if (autoUpdate) hideAll();
        }
    }

    public void updateVisibility() {
        if (!registered) return;
        if (!autoUpdate) return;

        synchronized (vas) {
            Location vasLoc = vas.getLocation();
            World world = vasLoc.getWorld();

            for (Player player : world.getPlayers()) {
                if (player.isDead()) continue;
                if (!NMSUtil.isRealPlayer(player)) continue;

                boolean isInRange = Utils.isInDisplayRange(player, vasLoc, getDisplayRange());

                if (isInRange) {
                    if (!isShown(player)) {
                        Predicate<Player> filter = getCanSeeFilter();
                        if (filter == null || filter.test(player)) {
                            show(player);
                        }
                    }
                } else {
                    hide(player);
                }
            }
        }
    }

    void updateData(UpdateFlag @Nullable [] flags) {
        synchronized (vas) {
            int entityId = vas.getEntityId();
            ArmorStandData data = vas.getData();

            for (Player player : viewers) {
                NMSUtil.updateMetadata(player, entityId, data, flags);
            }
        }
    }

    void updateEquipment(@NotNull EquipmentSlot slot, @Nullable ItemStack item) {
        synchronized (vas) {
            int entityId = vas.getEntityId();

            for (Player player : viewers) {
                NMSUtil.sendEquipment(player, entityId, new Map.Entry[]{new AbstractMap.SimpleEntry<>(slot, item)}, false);
            }
        }
    }

    void updateLocation() {
        synchronized (vas) {
            updateVisibility();

            int entityId = vas.getEntityId();
            Location location = vas.getLocation();

            for (Player player : viewers) {
                NMSUtil.teleportEntity(player, entityId, location);
            }
        }
    }
}
