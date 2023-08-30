package ru.violence.xholo.api.registry.impl;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.VirtualArmorStand;
import ru.violence.xholo.api.registry.HologramRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class HologramRegistryImpl implements HologramRegistry {
    private final XHoloPlugin plugin;
    private final Set<VirtualArmorStand> armorStands = new HashSet<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public HologramRegistryImpl(XHoloPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(@NotNull VirtualArmorStand vas) {
        Check.notNull(vas, "VAS is null");
        lock.writeLock().lock();
        armorStands.add(vas);
        lock.writeLock().unlock();
    }

    public void unregister(@NotNull VirtualArmorStand vas) {
        Check.notNull(vas, "VAS is null");
        lock.writeLock().lock();
        armorStands.remove(vas);
        lock.writeLock().unlock();
    }

    @Override
    public @NotNull List<VirtualArmorStand> getAllFrom(@NotNull World world) {
        Check.notNull(world, "World is null");
        List<VirtualArmorStand> list = new ArrayList<>();

        lock.readLock().lock();
        for (VirtualArmorStand vas : armorStands) {
            if (vas.getLocation().getWorld().equals(world)) {
                list.add(vas);
            }
        }
        lock.readLock().unlock();

        return list;
    }

    @Override
    public @NotNull List<VirtualArmorStand> getAll() {
        lock.readLock().lock();
        List<VirtualArmorStand> list = new ArrayList<>(armorStands);
        lock.readLock().unlock();

        return list;
    }
}
