package ru.violence.xholo.api;

import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import ru.violence.xholo.api.impl.BlockDisplayDataBuilderImpl;
import ru.violence.xholo.api.impl.BlockDisplayDataImpl;

public interface BlockDisplayDataBuilder extends DisplayDataBuilder {
    @Contract(value = "-> new", pure = true)
    static @NotNull BlockDisplayDataBuilder builder() {
        return new BlockDisplayDataBuilderImpl();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull BlockDisplayDataBuilder from(@NotNull BlockDisplayData data) {
        return new BlockDisplayDataBuilderImpl((BlockDisplayDataImpl) data);
    }

    @Contract(value = "_ -> this")
    @NotNull BlockDisplayDataBuilder visible(boolean visible);

    @Contract(value = "_ -> this")
    @NotNull BlockDisplayDataBuilder glowing(boolean glowing);

    @Override
    @NotNull BlockDisplayDataBuilder transformation(@NotNull Transformation transformation);

    @Override
    @NotNull BlockDisplayDataBuilder interpolationDuration(int duration);

    @Override
    @NotNull BlockDisplayDataBuilder teleportDuration(@Range(from = 0, to = 59) int duration);

    @Override
    @NotNull BlockDisplayDataBuilder shadowRadius(float radius);

    @Override
    @NotNull BlockDisplayDataBuilder shadowStrength(float strength);

    @Override
    @NotNull BlockDisplayDataBuilder displayWidth(float width);

    @Override
    @NotNull BlockDisplayDataBuilder displayHeight(float height);

    @Override
    @NotNull BlockDisplayDataBuilder interpolationDelay(int delay);

    @Override
    @NotNull BlockDisplayDataBuilder billboard(Display.@NotNull Billboard billboard);

    @Override
    @NotNull BlockDisplayDataBuilder glowColorOverride(@Nullable Color color);

    @Override
    @NotNull BlockDisplayDataBuilder brightness(Display.@Nullable Brightness brightness);

    @Contract(pure = true)
    @NotNull BlockData blockData();

    @Contract(value = "_ -> this")
    @NotNull BlockDisplayDataBuilder blockData(@NotNull BlockData block);

    @Contract(pure = true)
    @NotNull BlockDisplayData build();
}
