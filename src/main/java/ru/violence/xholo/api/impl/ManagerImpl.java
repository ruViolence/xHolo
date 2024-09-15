package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.BlockDisplayData;
import ru.violence.xholo.api.InteractionData;
import ru.violence.xholo.api.ItemDisplayData;
import ru.violence.xholo.api.Manager;
import ru.violence.xholo.api.TextDisplayData;
import ru.violence.xholo.api.VirtualArmorStand;
import ru.violence.xholo.api.VirtualBlockDisplay;
import ru.violence.xholo.api.VirtualEntity;
import ru.violence.xholo.api.VirtualInteraction;
import ru.violence.xholo.api.VirtualItemDisplay;
import ru.violence.xholo.api.VirtualTextDisplay;
import ru.violence.xholo.util.Utils;
import ru.violence.xholo.util.nms.NMSUtil;
import ru.violence.xholo.util.updateflags.UpdateFlag;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ManagerImpl implements Manager {
    private final VirtualEntity virtualEntity;
    private final @NotNull Set<Player> viewers = new HashSet<>();
    private double displayRange;
    private @Nullable Predicate<Player> canSeeFilter;
    private boolean registered;
    private boolean autoUpdate = true;
    private @Nullable Entity vehicle;

    public ManagerImpl(VirtualEntity virtualEntity) {
        this.virtualEntity = virtualEntity;
        this.displayRange = DEFAULT_DISPLAY_RANGE;
    }

    public ManagerImpl(VirtualEntity virtualEntity, double displayRange) {
        this.virtualEntity = virtualEntity;
        this.displayRange = displayRange;
    }

    @Override
    public boolean isShown(@NotNull Player player) {
        Check.notNull(player, "Player is null");
        synchronized (virtualEntity) {
            return viewers.contains(player);
        }
    }

    @Override
    public boolean show(@NotNull Player player) {
        synchronized (virtualEntity) {
            if (isShown(player)) return false;

            viewers.add(player);

            int entityId = virtualEntity.getEntityId();

            if (virtualEntity instanceof VirtualArmorStand vas) {
                NMSUtil.spawnEntityArmorStand(player, entityId, vas.getLocation(), vas.getData(),
                        vas.getItemInHand(),
                        vas.getItemInOffHand(),
                        vas.getHelmet(),
                        vas.getChestplate(),
                        vas.getLeggings(),
                        vas.getBoots());
            } else if (virtualEntity instanceof VirtualBlockDisplay vbd) {
                NMSUtil.spawnEntityBlockDisplay(player, entityId, vbd.getLocation(), vbd.getData());
            } else if (virtualEntity instanceof VirtualItemDisplay vid) {
                NMSUtil.spawnEntityItemDisplay(player, entityId, vid.getLocation(), vid.getData());
            } else if (virtualEntity instanceof VirtualTextDisplay vtd) {
                NMSUtil.spawnEntityTextDisplay(player, entityId, vtd.getLocation(), vtd.getData());
            } else if (virtualEntity instanceof VirtualInteraction vi) {
                NMSUtil.spawnEntityInteraction(player, entityId, vi.getLocation(), vi.getData());
            } else {
                throw new IllegalStateException("Unknown entity type: " + virtualEntity.getClass().getSimpleName());
            }

            return true;
        }
    }

    public boolean showAsPassenger(@NotNull Player player, @NotNull Entity vehicle) {
        synchronized (virtualEntity) {
            if (isShown(player)) return false;

            viewers.add(player);

            int entityId = virtualEntity.getEntityId();

            Location vehicleLocation = vehicle.getLocation();
            Location virtualEntityLocation = virtualEntity.getLocation();

            vehicleLocation.setYaw(virtualEntityLocation.getYaw());
            vehicleLocation.setPitch(virtualEntityLocation.getPitch());

            if (virtualEntity instanceof VirtualArmorStand vas) {
                NMSUtil.spawnEntityArmorStand(player, entityId, vehicleLocation, vas.getData(), vehicle,
                        vas.getItemInHand(),
                        vas.getItemInOffHand(),
                        vas.getHelmet(),
                        vas.getChestplate(),
                        vas.getLeggings(),
                        vas.getBoots());
            } else if (virtualEntity instanceof VirtualBlockDisplay vbd) {
                NMSUtil.spawnEntityBlockDisplay(player, entityId, vehicleLocation, vbd.getData(), vehicle);
            } else if (virtualEntity instanceof VirtualItemDisplay vid) {
                NMSUtil.spawnEntityItemDisplay(player, entityId, vehicleLocation, vid.getData(), vehicle);
            } else if (virtualEntity instanceof VirtualTextDisplay vtd) {
                NMSUtil.spawnEntityTextDisplay(player, entityId, vehicleLocation, vtd.getData(), vehicle);
            } else if (virtualEntity instanceof VirtualInteraction vi) {
                NMSUtil.spawnEntityInteraction(player, entityId, vehicleLocation, vi.getData(), vehicle);
            } else {
                throw new IllegalStateException("Unknown entity type: " + virtualEntity.getClass().getSimpleName());
            }

            return true;
        }
    }

    @Override
    public boolean hide(@NotNull Player player) {
        return hide(player, true);
    }

    public boolean hide(@NotNull Player player, boolean sendPacket) {
        synchronized (virtualEntity) {
            if (!isShown(player)) return false;

            viewers.remove(player);
            if (sendPacket) NMSUtil.destroyEntities(player, virtualEntity.getEntityId());

            return true;
        }
    }

    @Override
    public void hideAll() {
        synchronized (virtualEntity) {
            for (Player player : viewers) {
                NMSUtil.destroyEntities(player, virtualEntity.getEntityId());
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
    public double getDisplayRange() {
        return displayRange;
    }

    @Override
    public void setDisplayRange(double displayRange) {
        this.displayRange = Check.moreThan(displayRange, 0.0D, "Display range is 0 or less");
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
        synchronized (virtualEntity) {
            return viewers.size();
        }
    }

    @Override
    public boolean isVisibleFor(@NotNull Player player) {
        synchronized (virtualEntity) {
            return viewers.contains(player);
        }
    }

    @Override
    public @NotNull Set<Player> getViewers() {
        synchronized (virtualEntity) {
            return new HashSet<>(viewers);
        }
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    @Override
    public @Nullable Entity getVehicle() {
        synchronized (virtualEntity) {
            return vehicle;
        }
    }

    @Override
    public void setVehicle(@Nullable Entity vehicle) {
        synchronized (virtualEntity) {
            hideAll();
            this.vehicle = vehicle;
        }
    }

    @Override
    public void register() {
        synchronized (virtualEntity) {
            if (registered) return;
            XHoloPlugin.getInstance().getRegistry().register(virtualEntity);
            registered = true;

            updateVisibility();
        }
    }

    @Override
    public void unregister() {
        synchronized (virtualEntity) {
            if (!registered) return;
            XHoloPlugin.getInstance().getRegistry().unregister(virtualEntity);
            registered = false;

            if (autoUpdate) hideAll();
        }
    }

    public void updateVisibility() {
        updateVisibility(null, null);
    }

    public void updateVisibility(@Nullable Map<World, List<Player>> worldPlayersCache, @Nullable Consumer<Player> onUpdate) {
        if (!registered) return;
        if (!autoUpdate) return;

        synchronized (virtualEntity) {
            Entity vehicle = getVehicle();
            if (vehicle != null) {
                if (!vehicle.isValid() || !vehicle.isEmpty()) {
                    hideAll();
                    return;
                }

                Set<Player> trackedBy = vehicle.getTrackedBy();

                for (Player viewer : getViewers()) {
                    if (!trackedBy.contains(viewer)) {
                        hide(viewer);
                    }
                }

                for (Player player : trackedBy) {
                    if (player.equals(vehicle)) {
                        hide(player);
                        continue;
                    }

                    boolean passedFilter = isPassingCanSeeFilter(player);

                    if (isShown(player)) {
                        if (!passedFilter) {
                            hide(player);
                        }
                    } else {
                        if (passedFilter) {
                            showAsPassenger(player, vehicle);
                        }
                    }
                }

                return;
            }

            Location veLoc = virtualEntity.getLocation();
            World world = veLoc.getWorld();
            List<Player> players = worldPlayersCache != null ? worldPlayersCache.computeIfAbsent(world, World::getPlayers) : world.getPlayers();

            for (Player player : players) {
                if (!NMSUtil.isRealPlayer(player)) continue;
                if (NMSUtil.isRemoved(player)) continue;

                boolean isInRange = Utils.isInDisplayRange(player, veLoc, getDisplayRange());

                if (isInRange) {
                    boolean passedFilter = isPassingCanSeeFilter(player);

                    if (isShown(player)) {
                        if (!passedFilter) {
                            hide(player);
                        } else {
                            if (onUpdate != null) onUpdate.accept(player);
                        }
                    } else {
                        if (passedFilter) {
                            show(player);
                        }
                    }
                } else {
                    hide(player);
                }
            }
        }
    }

    public boolean isPassingCanSeeFilter(@NotNull Player player) {
        Predicate<Player> filter = getCanSeeFilter();
        return filter == null || filter.test(player);
    }

    void updateData(@NotNull List<UpdateFlag<?>> flags) {
        synchronized (virtualEntity) {
            int entityId = virtualEntity.getEntityId();

            if (virtualEntity instanceof VirtualArmorStandImpl vas) {
                ArmorStandData data = vas.getData();

                for (Player player : viewers) {
                    NMSUtil.updateArmorStandMetadata(player, entityId, data, flags);
                }
            } else if (virtualEntity instanceof VirtualBlockDisplayImpl vbd) {
                BlockDisplayData data = vbd.getData();

                for (Player player : viewers) {
                    NMSUtil.updateBlockDisplayMetadata(player, entityId, data, flags);
                }
            } else if (virtualEntity instanceof VirtualItemDisplayImpl vid) {
                ItemDisplayData data = vid.getData();

                for (Player player : viewers) {
                    NMSUtil.updateItemDisplayMetadata(player, entityId, data, flags);
                }
            } else if (virtualEntity instanceof VirtualTextDisplayImpl vtd) {
                TextDisplayData data = vtd.getData();

                for (Player player : viewers) {
                    NMSUtil.updateTextDisplayMetadata(player, entityId, data, flags);
                }
            } else if (virtualEntity instanceof VirtualInteractionImpl vi) {
                InteractionData data = vi.getData();

                for (Player player : viewers) {
                    NMSUtil.updateInteractionMetadata(player, entityId, data, flags);
                }
            } else {
                throw new IllegalStateException("Unknown entity type: " + virtualEntity.getClass().getSimpleName());
            }
        }
    }

    void updateEquipment(@NotNull EquipmentSlot slot, @Nullable ItemStack item) {
        synchronized (virtualEntity) {
            int entityId = virtualEntity.getEntityId();

            for (Player player : viewers) {
                NMSUtil.sendEquipment(player, entityId, new Map.Entry[]{new AbstractMap.SimpleEntry<>(slot, item)}, false);
            }
        }
    }

    void updateLocation(boolean isWorldChanged) {
        synchronized (virtualEntity) {
            if (isWorldChanged) {
                hideAll();
            }

            updateVisibility(null, player -> {
                int entityId = virtualEntity.getEntityId();
                Location location = virtualEntity.getLocation();

                NMSUtil.teleportEntity(player, entityId, location);
            });
        }
    }
}
