package ru.violence.xholo.api;

import org.jetbrains.annotations.Contract;

public interface VirtualEntityData {
    @Contract(pure = true)
    boolean isVisible();

    @Contract(pure = true)
    boolean isGlowing();
}
