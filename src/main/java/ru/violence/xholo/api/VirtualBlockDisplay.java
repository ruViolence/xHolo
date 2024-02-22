package ru.violence.xholo.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface VirtualBlockDisplay extends VirtualEntity {
    @Contract(pure = true)
    @NotNull BlockDisplayData getData();

    void setData(@NotNull BlockDisplayData data);
}
