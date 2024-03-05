package ru.violence.xholo.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface VirtualInteraction extends VirtualEntity {
    @Contract(pure = true)
    @NotNull InteractionData getData();

    void setData(@NotNull InteractionData data);
}
