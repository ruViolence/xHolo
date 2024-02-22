package ru.violence.xholo.api;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import ru.violence.xholo.api.impl.ItemDisplayDataBuilderImpl;
import ru.violence.xholo.api.impl.ItemDisplayDataImpl;

public interface ItemDisplayDataBuilder extends DisplayDataBuilder {
    @Contract(value = "-> new", pure = true)
    static @NotNull ItemDisplayDataBuilder builder() {
        return new ItemDisplayDataBuilderImpl();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ItemDisplayDataBuilder from(@NotNull ItemDisplayData data) {
        return new ItemDisplayDataBuilderImpl((ItemDisplayDataImpl) data);
    }

    @Contract(value = "_ -> this")
    @NotNull ItemDisplayDataBuilder visible(boolean visible);

    @Contract(value = "_ -> this")
    @NotNull ItemDisplayDataBuilder glowing(boolean glowing);

    @Override
    @NotNull ItemDisplayDataBuilder transformation(@NotNull Transformation transformation);

    @Override
    @NotNull ItemDisplayDataBuilder interpolationDuration(int duration);

    @Override
    @NotNull ItemDisplayDataBuilder teleportDuration(@Range(from = 0, to = 59) int duration);

    @Override
    @NotNull ItemDisplayDataBuilder shadowRadius(float radius);

    @Override
    @NotNull ItemDisplayDataBuilder shadowStrength(float strength);

    @Override
    @NotNull ItemDisplayDataBuilder displayWidth(float width);

    @Override
    @NotNull ItemDisplayDataBuilder displayHeight(float height);

    @Override
    @NotNull ItemDisplayDataBuilder interpolationDelay(int delay);

    @Override
    @NotNull ItemDisplayDataBuilder billboard(Display.@NotNull Billboard billboard);

    @Override
    @NotNull ItemDisplayDataBuilder glowColorOverride(@Nullable Color color);

    @Override
    @NotNull ItemDisplayDataBuilder brightness(Display.@Nullable Brightness brightness);

    @Contract(pure = true)
    @Nullable ItemStack itemStack();

    @Contract(value = "_ -> this")
    @NotNull ItemDisplayDataBuilder itemStack(@Nullable ItemStack item);

    @Contract(pure = true)
    ItemDisplay.@NotNull ItemDisplayTransform displayTransform();

    @Contract(value = "_ -> this")
    @NotNull ItemDisplayDataBuilder displayTransform(ItemDisplay.@NotNull ItemDisplayTransform displayTransform);

    @Contract(pure = true)
    @NotNull ItemDisplayData build();
}
