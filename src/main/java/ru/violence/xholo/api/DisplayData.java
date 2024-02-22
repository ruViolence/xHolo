package ru.violence.xholo.api;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayData extends VirtualEntityData {
    @Contract(pure = true)
    @NotNull Transformation getTransformation();

    @Contract(pure = true)
    int getInterpolationDuration();

    @Contract(pure = true)
    int getTeleportDuration();

    @Contract(pure = true)
    float getShadowRadius();

    @Contract(pure = true)
    float getShadowStrength();

    @Contract(pure = true)
    float getDisplayWidth();

    @Contract(pure = true)
    float getDisplayHeight();

    @Contract(pure = true)
    int getInterpolationDelay();

    @Contract(pure = true)
    @NotNull Display.Billboard getBillboard();

    @Contract(pure = true)
    @Nullable Color getGlowColorOverride();

    @Contract(pure = true)
    @Nullable Display.Brightness getBrightness();

    @Contract(pure = true)
    @NotNull DisplayDataBuilder modify();
}
