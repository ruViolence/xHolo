package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.Manager;
import ru.violence.xholo.api.VirtualArmorStand;
import ru.violence.xholo.util.nms.NMSUtil;
import ru.violence.xholo.util.updateflags.UpdateFlag;
import ru.violence.xholo.util.updateflags.UpdateFlags;

import java.util.List;

public final class VirtualArmorStandImpl implements VirtualArmorStand {
    private final int id = NMSUtil.getNextEntityId();
    private final ManagerImpl manager;

    private final @NotNull Plugin plugin;
    private @NotNull Location location;
    private @NotNull ArmorStandData data;
    private @Nullable ItemStack itemInHand;
    private @Nullable ItemStack itemInOffHand;
    private @Nullable ItemStack boots;
    private @Nullable ItemStack leggings;
    private @Nullable ItemStack chestplate;
    private @Nullable ItemStack helmet;

    public VirtualArmorStandImpl(@NotNull Plugin plugin, @NotNull Location location, @NotNull ArmorStandData data,
                                 @Nullable ItemStack itemInHand,
                                 @Nullable ItemStack itemInOffHand,
                                 @Nullable ItemStack boots,
                                 @Nullable ItemStack leggings,
                                 @Nullable ItemStack chestplate,
                                 @Nullable ItemStack helmet) {
        this.plugin = Check.notNull(plugin, "Plugin is null");
        this.location = Check.notNull(location, "Location is null");
        this.data = Check.notNull(data, "Data is null");
        Check.notNull(location.getWorld(), "World is null");
        this.itemInHand = itemInHand;
        this.itemInOffHand = itemInOffHand;
        this.boots = boots;
        this.leggings = leggings;
        this.chestplate = chestplate;
        this.helmet = helmet;
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
            boolean isWorldChanged = !this.location.getWorld().equals(location.getWorld());
            boolean isLocationChanged = isWorldChanged || !this.location.equals(location);
            this.location = location;
            if (isLocationChanged) {
                manager.updateLocation(isWorldChanged);
            }
        }
    }

    @Override
    public @NotNull Manager manager() {
        return manager;
    }

    @Override
    public @NotNull ArmorStandData getData() {
        return data;
    }

    @Override
    public void setData(@NotNull ArmorStandData data) {
        Check.notNull(data, "Data is null");

        synchronized (this) {
            ArmorStandData old = this.data;
            this.data = data;

            if (manager.getViewersAmount() != 0) {
                List<UpdateFlag<?>> flags = UpdateFlags.compareArmorStandData(old, data);
                if (flags.isEmpty()) return;

                manager.updateData(flags);
            }
        }
    }

    @Override
    public @Nullable ItemStack getItemInHand() {
        return itemInHand;
    }

    @Override
    public void setItemInHand(@Nullable ItemStack item) {
        synchronized (this) {
            this.itemInHand = item;
            manager.updateEquipment(EquipmentSlot.HAND, item);
        }
    }

    @Override
    public @Nullable ItemStack getItemInOffHand() {
        return itemInOffHand;
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack item) {
        synchronized (this) {
            this.itemInOffHand = item;
            manager.updateEquipment(EquipmentSlot.OFF_HAND, item);
        }
    }

    @Override
    public @Nullable ItemStack getBoots() {
        return boots;
    }

    @Override
    public void setBoots(@Nullable ItemStack item) {
        synchronized (this) {
            this.boots = item;
            manager.updateEquipment(EquipmentSlot.FEET, item);
        }
    }

    @Override
    public @Nullable ItemStack getLeggings() {
        return leggings;
    }

    @Override
    public void setLeggings(@Nullable ItemStack item) {
        synchronized (this) {
            this.leggings = item;
            manager.updateEquipment(EquipmentSlot.LEGS, item);
        }
    }

    @Override
    public @Nullable ItemStack getChestplate() {
        return chestplate;
    }

    @Override
    public void setChestplate(@Nullable ItemStack item) {
        synchronized (this) {
            this.chestplate = item;
            manager.updateEquipment(EquipmentSlot.CHEST, item);
        }
    }

    @Override
    public @Nullable ItemStack getHelmet() {
        return helmet;
    }

    @Override
    public void setHelmet(@Nullable ItemStack item) {
        synchronized (this) {
            this.helmet = item;
            manager.updateEquipment(EquipmentSlot.HEAD, item);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VirtualArmorStandImpl that = (VirtualArmorStandImpl) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
