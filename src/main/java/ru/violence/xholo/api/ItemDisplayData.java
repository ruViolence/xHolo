package ru.violence.xholo.api;

import org.bukkit.entity.ItemDisplay;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemDisplayData extends DisplayData {
    @Contract(pure = true)
    @Nullable CustomItem getItem();

    @Contract(pure = true)
    ItemDisplay.@NotNull ItemDisplayTransform getDisplayTransform();

    @Override
    @Contract(pure = true)
    @NotNull ItemDisplayDataBuilder modify();
}
