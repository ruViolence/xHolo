package ru.violence.xholo.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface VirtualEntity {
    @Contract(pure = true)
    @NotNull Plugin getPlugin();

    @Contract(pure = true)
    int getEntityId();

    @Deprecated
    @Contract(pure = true)
    @NotNull Location getLocation();

    @Deprecated
    void setLocation(@NotNull Location location);

    @Contract(pure = true)
    @NotNull World getWorld();

    void setWorld(@NotNull World world);

    @Contract(pure = true)
    double getX();

    @Contract(pure = true)
    double getY();

    @Contract(pure = true)
    double getZ();

    @Contract(pure = true)
    float getYaw();

    @Contract(pure = true)
    float getPitch();

    void teleport(double x, double y, double z, float yaw, float pitch);

    void setPosRot(double x, double y, double z, float yaw, float pitch);

    void setPos(double x, double y, double z);

    void setRot(float yaw, float pitch);

    @Contract(pure = true)
    @NotNull Manager manager();
}
