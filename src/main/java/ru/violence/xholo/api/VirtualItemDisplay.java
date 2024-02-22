package ru.violence.xholo.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface VirtualItemDisplay extends VirtualEntity {
    @Contract(pure = true)
    @NotNull ItemDisplayData getData();

    void setData(@NotNull ItemDisplayData data);
}
