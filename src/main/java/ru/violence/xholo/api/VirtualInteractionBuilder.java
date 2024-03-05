package ru.violence.xholo.api;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VirtualInteractionBuilder extends VirtualEntityBuilder {
    @Override
    @Contract(value = "_ -> this")
    @NotNull VirtualInteractionBuilder location(@NotNull Location location);

    @Contract(pure = true)
    @Nullable InteractionData data();

    @Contract(value = "_ -> this")
    @NotNull VirtualInteractionBuilder data(@NotNull InteractionData data);

    @Contract(value = "-> new", pure = true)
    @NotNull VirtualInteraction build();
}
