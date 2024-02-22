package ru.violence.xholo.api.impl;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.DisplayData;
import ru.violence.xholo.util.UniqueInt;

@Getter
public abstract class DisplayDataImpl extends VirtualEntityDataImpl implements DisplayData {
    protected final @NotNull Transformation transformation;
    protected final int interpolationDuration;
    protected final int teleportDuration;
    protected final float shadowRadius;
    protected final float shadowStrength;
    protected final float displayWidth;
    protected final float displayHeight;
    protected final @NotNull UniqueInt uniqueInterpolationDelay;
    protected final @NotNull Display.Billboard billboard;
    protected final @Nullable Color glowColorOverride;
    protected final @Nullable Display.Brightness brightness;

    public DisplayDataImpl(boolean visible,
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
                           @Nullable Display.Brightness brightness) {
        super(visible, glowing);
        this.transformation = Check.notNull(transformation, "Transformation is null");
        this.interpolationDuration = interpolationDuration;
        this.teleportDuration = teleportDuration;
        this.shadowRadius = shadowRadius;
        this.shadowStrength = shadowStrength;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.uniqueInterpolationDelay = uniqueInterpolationDelay;
        this.billboard = Check.notNull(billboard, "Billboard is null");
        this.glowColorOverride = glowColorOverride;
        this.brightness = brightness;
    }

    @Override
    public int getInterpolationDelay() {
        return uniqueInterpolationDelay.get();
    }
}
