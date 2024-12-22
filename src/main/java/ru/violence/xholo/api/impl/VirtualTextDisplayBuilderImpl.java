package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.TextDisplayData;
import ru.violence.xholo.api.VirtualTextDisplay;
import ru.violence.xholo.api.VirtualTextDisplayBuilder;
import ru.violence.xholo.util.Utils;

public final class VirtualTextDisplayBuilderImpl implements VirtualTextDisplayBuilder {
    private final @NotNull Plugin plugin;
    private @Nullable World world;
    private double x = Utils.UNSET_DOUBLE;
    private double y = Utils.UNSET_DOUBLE;
    private double z = Utils.UNSET_DOUBLE;
    private float yaw = Utils.UNSET_FLOAT;
    private float pitch = Utils.UNSET_FLOAT;
    private @Nullable TextDisplayData data;

    public VirtualTextDisplayBuilderImpl(@NotNull Plugin plugin) {
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
    public @NotNull VirtualTextDisplayBuilder location(@NotNull Location location) {
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
    public @NotNull VirtualTextDisplayBuilder world(@NotNull World world) {
        this.world = Check.notNull(world, "World is null");
        return this;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public @NotNull VirtualTextDisplayBuilder x(double x) {
        this.x = x;
        return this;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public @NotNull VirtualTextDisplayBuilder y(double y) {
        this.y = y;
        return this;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public @NotNull VirtualTextDisplayBuilder z(double z) {
        this.z = z;
        return this;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @Override
    public @NotNull VirtualTextDisplayBuilder yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @Override
    public @NotNull VirtualTextDisplayBuilder pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public @NotNull VirtualTextDisplayBuilder posRot(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
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
