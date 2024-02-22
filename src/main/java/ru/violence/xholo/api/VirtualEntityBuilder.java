package ru.violence.xholo.api;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VirtualEntityBuilder {
    @Contract(pure = true)
    @Nullable Location location();

    @Contract(value = "_ -> this")
    @NotNull VirtualEntityBuilder location(@NotNull Location location);
}
