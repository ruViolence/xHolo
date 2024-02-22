package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.BlockDisplayData;
import ru.violence.xholo.api.VirtualBlockDisplay;
import ru.violence.xholo.api.VirtualBlockDisplayBuilder;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;

public final class VirtualBlockDisplayBuilderImpl implements VirtualBlockDisplayBuilder {
    private final @NotNull HologramRegistryImpl registry;
    private final @NotNull Plugin plugin;
    private @Nullable Location location;
    private @Nullable BlockDisplayData data;

    public VirtualBlockDisplayBuilderImpl(@NotNull Plugin plugin, @NotNull HologramRegistryImpl registry) {
        this.plugin = Check.notNull(plugin, "Plugin is null");
        this.registry = Check.notNull(registry, "Registry is null");
    }

    @Override
    public @Nullable Location location() {
        return location;
    }

    @Override
    public @NotNull VirtualBlockDisplayBuilder location(@NotNull Location location) {
        Check.notNull(location, "Location is null");
        Check.notNull(location.getWorld(), "World is null");
        this.location = location;
        return this;
    }

    @Override
    public @Nullable BlockDisplayData data() {
        return data;
    }

    @Override
    public @NotNull VirtualBlockDisplayBuilder data(@NotNull BlockDisplayData data) {
        this.data = Check.notNull(data, "Data is null");
        return this;
    }

    @Override
    public @NotNull VirtualBlockDisplay build() {
        return new VirtualBlockDisplayImpl(
                registry,
                plugin,
                location,
                data
        );
    }
}
