package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.VirtualArmorStand;
import ru.violence.xholo.api.VirtualArmorStandBuilder;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;

public final class VirtualArmorStandBuilderImpl implements VirtualArmorStandBuilder {
    private final @NotNull HologramRegistryImpl registry;
    private final @NotNull Plugin plugin;
    private @Nullable Location location;
    private @Nullable ArmorStandData data;
    private @Nullable ItemStack itemInHand;
    private @Nullable ItemStack itemInOffHand;
    private @Nullable ItemStack boots;
    private @Nullable ItemStack leggings;
    private @Nullable ItemStack chestplate;
    private @Nullable ItemStack helmet;

    public VirtualArmorStandBuilderImpl(@NotNull Plugin plugin, @NotNull HologramRegistryImpl registry) {
        this.plugin = Check.notNull(plugin, "Plugin is null");
        this.registry = Check.notNull(registry, "Registry is null");
    }

    @Override
    public @Nullable Location location() {
        return location;
    }

    @Override
    public @NotNull VirtualArmorStandBuilder location(@NotNull Location location) {
        Check.notNull(location, "Location is null");
        Check.notNull(location.getWorld(), "World is null");
        this.location = location;
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
                registry,
                plugin,
                location,
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
