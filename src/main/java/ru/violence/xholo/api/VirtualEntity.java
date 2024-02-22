package ru.violence.xholo.api;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface VirtualEntity {
    @Contract(pure = true)
    @NotNull Plugin getPlugin();

    @Contract(pure = true)
    int getEntityId();

    @Contract(pure = true)
    @NotNull Location getLocation();

    void setLocation(@NotNull Location location);

    @Contract(pure = true)
    @NotNull Manager manager();
}
