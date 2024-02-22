package ru.violence.xholo.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface VirtualDisplay extends VirtualEntity {
    @Contract(pure = true)
    @NotNull DisplayData getData();

    void setData(@NotNull DisplayData data);
}
