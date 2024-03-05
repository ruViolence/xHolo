package ru.violence.xholo.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface InteractionData extends VirtualEntityData {
    @Contract(pure = true)
    float getWidth();

    @Contract(pure = true)
    float getHeight();

    @Contract(pure = true)
    boolean isResponsive();

    @Contract(pure = true)
    @NotNull InteractionDataBuilder modify();
}
