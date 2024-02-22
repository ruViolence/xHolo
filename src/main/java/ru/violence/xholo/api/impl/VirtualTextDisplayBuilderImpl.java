package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.TextDisplayData;
import ru.violence.xholo.api.VirtualTextDisplay;
import ru.violence.xholo.api.VirtualTextDisplayBuilder;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;

public final class VirtualTextDisplayBuilderImpl implements VirtualTextDisplayBuilder {
    private final @NotNull HologramRegistryImpl registry;
    private final @NotNull Plugin plugin;
    private @Nullable Location location;
    private @Nullable TextDisplayData data;

    public VirtualTextDisplayBuilderImpl(@NotNull Plugin plugin, @NotNull HologramRegistryImpl registry) {
        this.plugin = Check.notNull(plugin, "Plugin is null");
        this.registry = Check.notNull(registry, "Registry is null");
    }

    @Override
    public @Nullable Location location() {
        return location;
    }

    @Override
    public @NotNull VirtualTextDisplayBuilder location(@NotNull Location location) {
        Check.notNull(location, "Location is null");
        Check.notNull(location.getWorld(), "World is null");
        this.location = location;
        return this;
    }

    @Override
    public @Nullable TextDisplayData data() {
        return data;
    }

    @Override
    public @NotNull VirtualTextDisplayBuilder data(@NotNull TextDisplayData data) {
        this.data = Check.notNull(data, "Data is null");
        return this;
    }

    @Override
    public @NotNull VirtualTextDisplay build() {
        return new VirtualTextDisplayImpl(
                registry,
                plugin,
                location,
                data
        );
    }
}
