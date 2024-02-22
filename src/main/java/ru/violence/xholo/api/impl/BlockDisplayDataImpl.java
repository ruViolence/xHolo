package ru.violence.xholo.api.impl;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.BlockDisplayData;
import ru.violence.xholo.api.BlockDisplayDataBuilder;
import ru.violence.xholo.util.UniqueInt;

@Getter
public final class BlockDisplayDataImpl extends DisplayDataImpl implements BlockDisplayData {
    private final @NotNull BlockData block;

    public BlockDisplayDataImpl(boolean visible,
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
                                @NotNull BlockData block) {
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
        this.block = Check.notNull(block, "Block is null");
    }

    @Override
    public @NotNull BlockDisplayDataBuilder modify() {
        return new BlockDisplayDataBuilderImpl(this);
    }
}
