package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.BlockDisplayData;
import ru.violence.xholo.api.Manager;
import ru.violence.xholo.api.VirtualBlockDisplay;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;
import ru.violence.xholo.util.nms.NMSUtil;
import ru.violence.xholo.util.updateflags.UpdateFlag;
import ru.violence.xholo.util.updateflags.UpdateFlags;

import java.util.List;

public final class VirtualBlockDisplayImpl implements VirtualBlockDisplay {
    private final int id = NMSUtil.getNextEntityId();
    private final HologramRegistryImpl registry;
    private final ManagerImpl manager;

    private final @NotNull Plugin plugin;
    private @NotNull Location location;
    private @NotNull BlockDisplayData data;

    public VirtualBlockDisplayImpl(@NotNull HologramRegistryImpl registry, @NotNull Plugin plugin, @NotNull Location location, @NotNull BlockDisplayData data) {
        this.registry = Check.notNull(registry, "Registry is null");
        this.plugin = Check.notNull(plugin, "Plugin is null");
        this.location = Check.notNull(location, "Location is null");
        this.data = Check.notNull(data, "Data is null");
        Check.notNull(location.getWorld(), "World is null");
        this.manager = new ManagerImpl(this);
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    @Override
    public int getEntityId() {
        return id;
    }

    @Override
    public @NotNull Location getLocation() {
        return location.clone();
    }

    @Override
    public void setLocation(@NotNull Location location) {
        Check.notNull(location, "Location is null");
        Check.notNull(location.getWorld(), "World is null");
        synchronized (this) {
            this.location = location;
            manager.updateLocation();
        }
    }

    @Override
    public @NotNull Manager manager() {
        return manager;
    }

    @Override
    public @NotNull BlockDisplayData getData() {
        return data;
    }

    @Override
    public void setData(@NotNull BlockDisplayData data) {
        Check.notNull(data, "Data is null");

        synchronized (this) {
            BlockDisplayData old = this.data;
            this.data = data;

            if (manager.getViewersAmount() != 0) {
                List<UpdateFlag<?>> flags = UpdateFlags.compareBlockDisplayData(old, data);
                if (flags.isEmpty()) return;

                manager.updateData(flags);
            }
        }
    }

    @NotNull HologramRegistryImpl getRegistry() {
        return registry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VirtualBlockDisplayImpl that = (VirtualBlockDisplayImpl) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
