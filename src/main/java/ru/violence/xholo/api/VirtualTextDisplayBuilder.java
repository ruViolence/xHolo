package ru.violence.xholo.api;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VirtualTextDisplayBuilder extends VirtualEntityBuilder {
    @Override
    @Contract(value = "_ -> this")
    @NotNull VirtualTextDisplayBuilder location(@NotNull Location location);

    @Contract(pure = true)
    @Nullable TextDisplayData data();

    @Contract(value = "_ -> this")
    @NotNull VirtualTextDisplayBuilder data(@NotNull TextDisplayData data);

    @Contract(value = "-> new", pure = true)
    @NotNull VirtualTextDisplay build();
}
