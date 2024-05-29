package ru.violence.xholo.api.impl;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.CustomItem;
import ru.violence.xholo.api.ItemDisplayData;
import ru.violence.xholo.api.ItemDisplayDataBuilder;
import ru.violence.xholo.util.UniqueInt;

@Getter
public final class ItemDisplayDataImpl extends DisplayDataImpl implements ItemDisplayData {
    private final @Nullable CustomItem item;
    private final ItemDisplay.@NotNull ItemDisplayTransform displayTransform;

    public ItemDisplayDataImpl(boolean visible,
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
                               @Nullable CustomItem item,
                               ItemDisplay.@NotNull ItemDisplayTransform displayTransform) {
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
        this.item = item;
        this.displayTransform = Check.notNull(displayTransform, "DisplayTransform is null");
    }

    @Override
    public @NotNull ItemDisplayDataBuilder modify() {
        return new ItemDisplayDataBuilderImpl(this);
    }
}
