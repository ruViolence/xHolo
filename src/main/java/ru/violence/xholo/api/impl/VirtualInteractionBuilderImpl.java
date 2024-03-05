package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.InteractionData;
import ru.violence.xholo.api.VirtualInteraction;
import ru.violence.xholo.api.VirtualInteractionBuilder;

public final class VirtualInteractionBuilderImpl implements VirtualInteractionBuilder {
    private final @NotNull Plugin plugin;
    private @Nullable Location location;
    private @Nullable InteractionData data;

    public VirtualInteractionBuilderImpl(@NotNull Plugin plugin) {
        this.plugin = Check.notNull(plugin, "Plugin is null");
    }

    @Override
    public @Nullable Location location() {
        return location;
    }

    @Override
    public @NotNull VirtualInteractionBuilder location(@NotNull Location location) {
        Check.notNull(location, "Location is null");
        Check.notNull(location.getWorld(), "World is null");
        this.location = location;
        return this;
    }

    @Override
    public @Nullable InteractionData data() {
        return data;
    }

    @Override
    public @NotNull VirtualInteractionBuilder data(@NotNull InteractionData data) {
        this.data = Check.notNull(data, "Data is null");
        return this;
    }

    @Override
    public @NotNull VirtualInteraction build() {
        return new VirtualInteractionImpl(
                plugin,
                location,
                data
        );
    }
}
