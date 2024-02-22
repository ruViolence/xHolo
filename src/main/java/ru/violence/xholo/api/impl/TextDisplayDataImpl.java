package ru.violence.xholo.api.impl;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.CustomName;
import ru.violence.xholo.api.TextDisplayData;
import ru.violence.xholo.api.TextDisplayDataBuilder;
import ru.violence.xholo.util.UniqueInt;

@Getter
public final class TextDisplayDataImpl extends DisplayDataImpl implements TextDisplayData {
    private final @NotNull CustomName text;
    private final int lineWidth;
    private final @Nullable Color backgroundColor;
    private final byte textOpacity;
    private final boolean shadowed;
    private final boolean seeThrough;
    private final boolean defaultBackground;
    private final TextDisplay.@NotNull TextAlignment alignment;

    public TextDisplayDataImpl(boolean visible,
                               boolean glowing,
                               @NotNull Transformation transformation,
                               int interpolationDuration,
                               int teleportDuration,
                               float shadowRadius,
                               float shadowStrength,
                               float displayWidth,
                               float displayHeight,
                               @NotNull UniqueInt uniqueInterpolationDelay,
                               @NotNull Display.Billboard billboard,
                               @Nullable Color glowColorOverride,
                               @Nullable Display.Brightness brightness,
                               @NotNull CustomName text,
                               int lineWidth,
                               @Nullable Color backgroundColor,
                               byte textOpacity,
                               boolean shadowed,
                               boolean seeThrough,
                               boolean defaultBackground,
                               @NotNull TextDisplay.@NotNull TextAlignment alignment) {
        super(visible,
                glowing,
                transformation,
                interpolationDuration,
                teleportDuration,
                shadowRadius,
                shadowStrength,
                displayWidth,
                displayHeight,
                uniqueInterpolationDelay,
                billboard,
                glowColorOverride,
                brightness);
        this.text = Check.notNull(text, "Text is null");
        this.lineWidth = lineWidth;
        this.backgroundColor = backgroundColor;
        this.textOpacity = textOpacity;
        this.shadowed = shadowed;
        this.seeThrough = seeThrough;
        this.defaultBackground = defaultBackground;
        this.alignment = Check.notNull(alignment, "Alignment is null");
    }

    @Override
    public @NotNull TextDisplayDataBuilder modify() {
        return new TextDisplayDataBuilderImpl(this);
    }
}
