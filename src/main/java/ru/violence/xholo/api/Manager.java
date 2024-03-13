package ru.violence.xholo.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;

public interface Manager {
    double DEFAULT_DISPLAY_RANGE = 32.0D;

    @Contract(pure = true)
    boolean isShown(@NotNull Player player);

    boolean show(@NotNull Player player);

    boolean hide(@NotNull Player player);

    void hideAll();

    @Contract(pure = true)
    @Nullable Predicate<Player> getCanSeeFilter();

    void setCanSeeFilter(@Nullable Predicate<Player> filter);

    @Contract(pure = true)
    double getDisplayRange();

    void setDisplayRange(double displayRange);

    @Contract(pure = true)
    boolean isAutoUpdate();

    void setAutoUpdate(boolean autoUpdate);

    @Contract(pure = true)
    int getViewersAmount();

    @Contract(pure = true)
    boolean isVisibleFor(@NotNull Player player);

    @Contract(value = "-> new", pure = true)
    @NotNull Set<Player> getViewers();

    @Contract(pure = true)
    boolean isRegistered();

    @Nullable Entity getVehicle();

    void setVehicle(@Nullable Entity vehicle);

    void register();

    void unregister();
}
