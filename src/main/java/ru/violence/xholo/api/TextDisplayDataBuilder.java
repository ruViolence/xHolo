package ru.violence.xholo.api;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import ru.violence.xholo.api.impl.TextDisplayDataBuilderImpl;
import ru.violence.xholo.api.impl.TextDisplayDataImpl;

public interface TextDisplayDataBuilder extends DisplayDataBuilder {
    @Contract(value = "-> new", pure = true)
    static @NotNull TextDisplayDataBuilder builder() {
        return new TextDisplayDataBuilderImpl();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull TextDisplayDataBuilder from(@NotNull TextDisplayData data) {
        return new TextDisplayDataBuilderImpl((TextDisplayDataImpl) data);
    }

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder visible(boolean visible);

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder glowing(boolean glowing);

    @Override
    @NotNull TextDisplayDataBuilder transformation(@NotNull Transformation transformation);

    @Override
    @NotNull TextDisplayDataBuilder interpolationDuration(int duration);

    @Override
    @NotNull TextDisplayDataBuilder teleportDuration(@Range(from = 0, to = 59) int duration);

    @Override
    @NotNull TextDisplayDataBuilder shadowRadius(float radius);

    @Override
    @NotNull TextDisplayDataBuilder shadowStrength(float strength);

    @Override
    @NotNull TextDisplayDataBuilder displayWidth(float width);

    @Override
    @NotNull TextDisplayDataBuilder displayHeight(float height);

    @Override
    @NotNull TextDisplayDataBuilder interpolationDelay(int delay);

    @Override
    @NotNull TextDisplayDataBuilder billboard(Display.@NotNull Billboard billboard);

    @Override
    @NotNull TextDisplayDataBuilder glowColorOverride(@Nullable Color color);

    @Override
    @NotNull TextDisplayDataBuilder brightness(Display.@Nullable Brightness brightness);

    @Contract(pure = true)
    @Nullable CustomName text();

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder text(@NotNull CustomName text);

    @Contract(pure = true)
    int lineWidth();

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder lineWidth(int lineWidth);

    @Contract(pure = true)
    @Nullable Color backgroundColor();

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder backgroundColor(@Nullable Color backgroundColor);

    @Contract(pure = true)
    int textOpacity();

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder textOpacity(byte textOpacity);

    @Contract(pure = true)
    boolean shadowed();

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder shadowed(boolean shadowed);

    @Contract(pure = true)
    boolean seeThrough();

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder seeThrough(boolean seeThrough);

    @Contract(pure = true)
    boolean defaultBackground();

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder defaultBackground(boolean defaultBackground);

    @Contract(pure = true)
    TextDisplay.@NotNull TextAlignment alignment();

    @Contract(value = "_ -> this")
    @NotNull TextDisplayDataBuilder alignment(TextDisplay.@NotNull TextAlignment alignment);

    @Contract(pure = true)
    @NotNull TextDisplayData build();
}
