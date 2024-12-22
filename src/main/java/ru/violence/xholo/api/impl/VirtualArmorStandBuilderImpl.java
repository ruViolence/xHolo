package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.VirtualArmorStand;
import ru.violence.xholo.api.VirtualArmorStandBuilder;
import ru.violence.xholo.api.VirtualEntityBuilder;
import ru.violence.xholo.util.Utils;

public final class VirtualArmorStandBuilderImpl implements VirtualArmorStandBuilder {

    private final @NotNull Plugin plugin;
    private @Nullable World world;
    private double x = Utils.UNSET_DOUBLE;
    private double y = Utils.UNSET_DOUBLE;
    private double z = Utils.UNSET_DOUBLE;
    private float yaw = Utils.UNSET_FLOAT;
    private float pitch = Utils.UNSET_FLOAT;
    private @Nullable ArmorStandData data;
    private @Nullable ItemStack itemInHand;
    private @Nullable ItemStack itemInOffHand;
    private @Nullable ItemStack boots;
    private @Nullable ItemStack leggings;
    private @Nullable ItemStack chestplate;
    private @Nullable ItemStack helmet;

    public VirtualArmorStandBuilderImpl(@NotNull Plugin plugin) {
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
    public @NotNull VirtualArmorStandBuilder location(@NotNull Location location) {
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
    public @NotNull VirtualEntityBuilder world(@NotNull World world) {
        this.world = Check.notNull(world, "World is null");
        return this;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public @NotNull VirtualEntityBuilder x(double x) {
        this.x = x;
        return this;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public @NotNull VirtualEntityBuilder y(double y) {
        this.y = y;
        return this;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public @NotNull VirtualEntityBuilder z(double z) {
        this.z = z;
        return this;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @Override
    public @NotNull VirtualEntityBuilder yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @Override
    public @NotNull VirtualEntityBuilder pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public @NotNull VirtualEntityBuilder posRot(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        return this;
    }

    @Override
    public @Nullable ArmorStandData data() {
        return data;
    }

    @Override
    public @NotNull VirtualArmorStandBuilder data(@NotNull ArmorStandData data) {
        this.data = Check.notNull(data, "Data is null");
        return this;
    }

    @Override
    public @Nullable ItemStack itemInHand() {
        return itemInHand;
    }

    @Override
    public @NotNull VirtualArmorStandBuilder itemInHand(@Nullable ItemStack item) {
        this.itemInHand = item;
        return this;
    }

    @Override
    public @Nullable ItemStack itemInOffHand() {
        return itemInOffHand;
    }

    @Override
    public @NotNull VirtualArmorStandBuilder itemInOffHand(@Nullable ItemStack item) {
        this.itemInOffHand = item;
        return this;
    }

    @Override
    public @Nullable ItemStack boots() {
        return boots;
    }

    @Override
    public @NotNull VirtualArmorStandBuilder boots(@Nullable ItemStack item) {
        this.boots = item;
        return this;
    }

    @Override
    public @Nullable ItemStack leggings() {
        return leggings;
    }

    @Override
    public @NotNull VirtualArmorStandBuilder leggings(@Nullable ItemStack item) {
        this.leggings = item;
        return this;
    }

    @Override
    public @Nullable ItemStack chestplate() {
        return chestplate;
    }

    @Override
    public @NotNull VirtualArmorStandBuilder chestplate(@Nullable ItemStack item) {
        this.chestplate = item;
        return this;
    }

    @Override
    public @Nullable ItemStack helmet() {
        return helmet;
    }

    @Override
    public @NotNull VirtualArmorStandBuilder helmet(@Nullable ItemStack item) {
        this.helmet = item;
        return this;
    }

    @Override
    public @NotNull VirtualArmorStand build() {
        return new VirtualArmorStandImpl(
                plugin,
                world,
                x,
                y,
                z,
                yaw,
                pitch,
                data,
                itemInHand,
                itemInOffHand,
                boots,
                leggings,
                chestplate,
                helmet
        );
    }
}
