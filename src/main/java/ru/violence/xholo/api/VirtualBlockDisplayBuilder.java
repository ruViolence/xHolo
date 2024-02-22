package ru.violence.xholo.api;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VirtualBlockDisplayBuilder extends VirtualEntityBuilder {
    @Override
    @Contract(value = "_ -> this")
    @NotNull VirtualBlockDisplayBuilder location(@NotNull Location location);

    @Contract(pure = true)
    @Nullable BlockDisplayData data();

    @Contract(value = "_ -> this")
    @NotNull VirtualBlockDisplayBuilder data(@NotNull BlockDisplayData data);

    @Contract(value = "-> new", pure = true)
    @NotNull VirtualBlockDisplay build();
}
