package ru.violence.xholo.api.registry.impl;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.VirtualEntity;
import ru.violence.xholo.api.registry.HologramRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class HologramRegistryImpl implements HologramRegistry {
    private final XHoloPlugin plugin;
    private final Map<Integer, VirtualEntity> virtualEntities = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public HologramRegistryImpl(XHoloPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@NotNull VirtualEntity ve) {
        Check.notNull(ve, "VirtualEntity is null");
        lock.writeLock().lock();
        virtualEntities.put(ve.getEntityId(), ve);
        lock.writeLock().unlock();
    }

    public void unregister(@NotNull VirtualEntity ve) {
        Check.notNull(ve, "VirtualEntity is null");
        lock.writeLock().lock();
        virtualEntities.remove(ve);
        lock.writeLock().unlock();
    }

    @Override
    public @Nullable VirtualEntity getFromId(int entityId) {
        lock.readLock().lock();
        for (VirtualEntity ve : virtualEntities.values()) {
            if (ve.getEntityId() == entityId) {
                return ve;
            }
        }
        lock.readLock().unlock();

        return null;
    }

    @Override
    public @NotNull List<VirtualEntity> getAllFrom(@NotNull World world) {
        Check.notNull(world, "World is null");
        List<VirtualEntity> list = new ArrayList<>();

        lock.readLock().lock();
        for (VirtualEntity ve : virtualEntities.values()) {
            if (ve.getLocation().getWorld().equals(world)) {
                list.add(ve);
            }
        }
        lock.readLock().unlock();

        return list;
    }

    @Override
    public @NotNull List<VirtualEntity> getAll() {
        lock.readLock().lock();
        List<VirtualEntity> list = new ArrayList<>(virtualEntities.values());
        lock.readLock().unlock();

        return list;
    }

    @Override
    public @NotNull List<VirtualEntity> getAllVisibleFor(@NotNull Player player) {
        Check.notNull(player, "Player is null");
        List<VirtualEntity> list = new ArrayList<>();

        lock.readLock().lock();
        for (VirtualEntity ve : virtualEntities.values()) {
            if (ve.manager().isVisibleFor(player)) {
                list.add(ve);
            }
        }
        lock.readLock().unlock();

        return list;
    }
}
