package ru.violence.xholo.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface VirtualTextDisplay extends VirtualEntity {
    @Contract(pure = true)
    @NotNull TextDisplayData getData();

    void setData(@NotNull TextDisplayData data);
}
