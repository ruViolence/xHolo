package ru.violence.xholo.api;

import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextDisplayData extends DisplayData {
    @Contract(pure = true)
    @NotNull CustomName getText();

    @Contract(pure = true)
    int getLineWidth();

    @Contract(pure = true)
    @Nullable Color getBackgroundColor();

    @Contract(pure = true)
    byte getTextOpacity();

    @Contract(pure = true)
    boolean isShadowed();

    @Contract(pure = true)
    boolean isSeeThrough();

    @Contract(pure = true)
    boolean isDefaultBackground();

    @Contract(pure = true)
    TextDisplay.@NotNull TextAlignment getAlignment();

    @Override
    @Contract(pure = true)
    @NotNull TextDisplayDataBuilder modify();
}
