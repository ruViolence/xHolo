package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ItemDisplayData;
import ru.violence.xholo.api.VirtualItemDisplay;
import ru.violence.xholo.api.VirtualItemDisplayBuilder;

public final class VirtualItemDisplayBuilderImpl implements VirtualItemDisplayBuilder {
    private final @NotNull Plugin plugin;
    private @Nullable Location location;
    private @Nullable ItemDisplayData data;

    public VirtualItemDisplayBuilderImpl(@NotNull Plugin plugin) {
        this.plugin = Check.notNull(plugin, "Plugin is null");
    }

    @Override
    public @Nullable Location location() {
        return location;
    }

    @Override
    public @NotNull VirtualItemDisplayBuilder location(@NotNull Location location) {
        Check.notNull(location, "Location is null");
        Check.notNull(location.getWorld(), "World is null");
        this.location = location;
        return this;
    }

    @Override
    public @Nullable ItemDisplayData data() {
        return data;
    }

    @Override
    public @NotNull VirtualItemDisplayBuilder data(@NotNull ItemDisplayData data) {
        this.data = Check.notNull(data, "Data is null");
        return this;
    }

    @Override
    public @NotNull VirtualItemDisplay build() {
        return new VirtualItemDisplayImpl(
                plugin,
                location,
                data
        );
    }
}
