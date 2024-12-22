package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.InteractionData;
import ru.violence.xholo.api.VirtualInteraction;
import ru.violence.xholo.api.VirtualInteractionBuilder;
import ru.violence.xholo.util.Utils;

public final class VirtualInteractionBuilderImpl implements VirtualInteractionBuilder {
    private final @NotNull Plugin plugin;
    private @Nullable World world;
    private double x = Utils.UNSET_DOUBLE;
    private double y = Utils.UNSET_DOUBLE;
    private double z = Utils.UNSET_DOUBLE;
    private float yaw = Utils.UNSET_FLOAT;
    private float pitch = Utils.UNSET_FLOAT;
    private @Nullable InteractionData data;

    public VirtualInteractionBuilderImpl(@NotNull Plugin plugin) {
        this.plugin = Check.notNull(plugin, "Plugin is null");
    }

    @Override
    public @Nullable Location location() {
        if (world == null) return null;
        if (Utils.isUnset(x) || Utils.isUnset(y) || Utils.isUnset(z)) return null;
        if (Utils.isUnset(yaw) || Utils.isUnset(pitch)) return null;

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull VirtualInteractionBuilder location(@NotNull Location location) {
        Check.notNull(location, "Location is null");
        Check.notNull(location.getWorld(), "World is null");
        this.world = location.getWorld();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        return this;
    }

    @Override
    public @Nullable World world() {
        return world;
    }

    @Override
    public @NotNull VirtualInteractionBuilder world(@NotNull World world) {
        this.world = Check.notNull(world, "World is null");
        return this;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public @NotNull VirtualInteractionBuilder x(double x) {
        this.x = x;
        return this;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public @NotNull VirtualInteractionBuilder y(double y) {
        this.y = y;
        return this;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public @NotNull VirtualInteractionBuilder z(double z) {
        this.z = z;
        return this;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @Override
    public @NotNull VirtualInteractionBuilder yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @Override
    public @NotNull VirtualInteractionBuilder pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public @NotNull VirtualInteractionBuilder posRot(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
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
                world,
                x,
                y,
                z,
                yaw,
                pitch,
                data
        );
    }
}
