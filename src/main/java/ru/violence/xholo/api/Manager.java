package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Set;
import java.util.function.Predicate;

public interface Manager {
    int DEFAULT_DISPLAY_RANGE = 32;

    @Contract(pure = true)
    boolean isShown(@NotNull Player player);

    boolean show(@NotNull Player player);

    boolean hide(@NotNull Player player);

    @Contract(pure = true)
    @Nullable Predicate<Player> getCanSeeFilter();

    void setCanSeeFilter(@Nullable Predicate<Player> filter);

    @Contract(pure = true)
    int getDisplayRange();

    void setDisplayRange(@Range(from = 1, to = Integer.MAX_VALUE) int displayRange);

    @Contract(pure = true)
    boolean isAutoUpdate();

    void setAutoUpdate(boolean autoUpdate);

    @Contract(pure = true)
    int getViewersAmount();

    @Contract(value = "-> new", pure = true)
    @NotNull Set<Player> getViewers();

    @Contract(pure = true)
    boolean isRegistered();

    void register();

    void unregister();
}
