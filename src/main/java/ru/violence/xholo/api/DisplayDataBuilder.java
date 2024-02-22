package ru.violence.xholo.api;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public interface DisplayDataBuilder {
    @Contract(pure = true)
    boolean visible();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder visible(boolean visible);

    @Contract(pure = true)
    boolean glowing();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder glowing(boolean glowing);

    @Contract(pure = true)
    @NotNull Transformation transformation();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder transformation(@NotNull Transformation transformation);

    @Contract(pure = true)
    int interpolationDuration();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder interpolationDuration(int duration);

    @Contract(pure = true)
    @Range(from = 0, to = 59) int teleportDuration();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder teleportDuration(@Range(from = 0, to = 59) int duration);

    @Contract(pure = true)
    float shadowRadius();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder shadowRadius(float radius);

    @Contract(pure = true)
    float shadowStrength();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder shadowStrength(float strength);

    @Contract(pure = true)
    float displayWidth();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder displayWidth(float width);

    @Contract(pure = true)
    float displayHeight();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder displayHeight(float height);

    @Contract(pure = true)
    int interpolationDelay();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder interpolationDelay(int delay);

    @Contract(pure = true)
    @NotNull Display.Billboard billboard();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder billboard(Display.@NotNull Billboard billboard);

    @Contract(pure = true)
    @Nullable Color glowColorOverride();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder glowColorOverride(@Nullable Color color);

    @Contract(pure = true)
    @Nullable Display.Brightness brightness();

    @Contract(value = "_ -> this")
    @NotNull DisplayDataBuilder brightness(Display.@Nullable Brightness brightness);

    @Contract(value = "-> new", pure = true)
    @NotNull DisplayData build();
}
