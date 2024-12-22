package ru.violence.xholo.api.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.Manager;
import ru.violence.xholo.api.VirtualArmorStand;
import ru.violence.xholo.util.Utils;
import ru.violence.xholo.util.nms.NMSUtil;
import ru.violence.xholo.util.updateflags.UpdateFlag;
import ru.violence.xholo.util.updateflags.UpdateFlags;

import java.util.List;

public final class VirtualArmorStandImpl implements VirtualArmorStand {
    private final int id = NMSUtil.getNextEntityId();
    private final ManagerImpl manager;

    private final @NotNull Plugin plugin;
    private @NotNull World world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private @NotNull ArmorStandData data;
    private @Nullable ItemStack itemInHand;
    private @Nullable ItemStack itemInOffHand;
    private @Nullable ItemStack boots;
    private @Nullable ItemStack leggings;
    private @Nullable ItemStack chestplate;
    private @Nullable ItemStack helmet;

    public VirtualArmorStandImpl(@NotNull Plugin plugin,
                                 @NotNull World world,
                                 double x,
                                 double y,
                                 double z,
                                 float yaw,
                                 float pitch,
                                 @NotNull ArmorStandData data,
                                 @Nullable ItemStack itemInHand,
                                 @Nullable ItemStack itemInOffHand,
                                 @Nullable ItemStack boots,
                                 @Nullable ItemStack leggings,
                                 @Nullable ItemStack chestplate,
                                 @Nullable ItemStack helmet) {
        Check.isTrue(!Utils.isUnset(x), "X is unset");
        Check.isTrue(!Utils.isUnset(y), "Y is unset");
        Check.isTrue(!Utils.isUnset(z), "Z is unset");
        Check.isTrue(!Utils.isUnset(yaw), "Yaw is unset");
        Check.isTrue(!Utils.isUnset(pitch), "Pitch is unset");

        this.plugin = Check.notNull(plugin, "Plugin is null");
        this.world = Check.notNull(world, "World is null");
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.data = Check.notNull(data, "Data is null");
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
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public void setLocation(@NotNull Location location) {
        Check.notNull(location, "Location is null");
        Check.notNull(location.getWorld(), "World is null");
        synchronized (this) {
            setWorld(location.getWorld());
            teleport(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }
    }

    @Override
    public @NotNull World getWorld() {
        return world;
    }

    @Override
    public void setWorld(@NotNull World world) {
        Check.notNull(world, "World is null");
        synchronized (this) {
            boolean isWorldChanged = !this.world.equals(world);

            // TODO: Test this
            if (isWorldChanged) {
                manager.hideAll();
            }
        }
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch) {
        synchronized (this) {
            boolean isChanged = this.x != x || this.y != y || this.z != z || this.yaw != yaw || this.pitch != pitch;

            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;

            if (isChanged) {
                manager.teleport(x, y, z, yaw, pitch);
            }
        }
    }

    @Override
    public void setPosRot(double x, double y, double z, float yaw, float pitch) {
        synchronized (this) {
            boolean isChanged = this.x != x || this.y != y || this.z != z || this.yaw != yaw || this.pitch != pitch;

            double oldX = this.x;
            double oldY = this.y;
            double oldZ = this.z;

            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;

            if (isChanged) {
                manager.setPosRot(oldX, oldY, oldZ, x, y, z, yaw, pitch);
            }
        }
    }

    @Override
    public void setPos(double x, double y, double z) {
        synchronized (this) {
            boolean isChanged = this.x != x || this.y != y || this.z != z;

            double oldX = this.x;
            double oldY = this.y;
            double oldZ = this.z;

            this.x = x;
            this.y = y;
            this.z = z;

            if (isChanged) {
                manager.setPos(oldX, oldY, oldZ, x, y, z);
            }
        }
    }

    @Override
    public void setRot(float yaw, float pitch) {
        synchronized (this) {
            boolean isChanged = this.yaw != yaw || this.pitch != pitch;

            this.yaw = yaw;
            this.pitch = pitch;

            if (isChanged) {
                manager.setRot(yaw, pitch);
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
