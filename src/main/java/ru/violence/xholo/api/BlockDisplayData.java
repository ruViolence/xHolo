package ru.violence.xholo.api;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface BlockDisplayData extends DisplayData {
    @Contract(pure = true)
    @NotNull BlockData getBlock();

    @Override
    @Contract(pure = true)
    @NotNull BlockDisplayDataBuilder modify();
}
