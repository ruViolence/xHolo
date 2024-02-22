package ru.violence.xholo.api;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VirtualItemDisplayBuilder extends VirtualEntityBuilder {
    @Override
    @Contract(value = "_ -> this")
    @NotNull VirtualItemDisplayBuilder location(@NotNull Location location);

    @Contract(pure = true)
    @Nullable ItemDisplayData data();

    @Contract(value = "_ -> this")
    @NotNull VirtualItemDisplayBuilder data(@NotNull ItemDisplayData data);

    @Contract(value = "-> new", pure = true)
    @NotNull VirtualItemDisplay build();
}
