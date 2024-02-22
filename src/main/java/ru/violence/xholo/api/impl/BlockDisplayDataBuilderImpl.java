package ru.violence.xholo.api.impl;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.BlockDisplayData;
import ru.violence.xholo.api.BlockDisplayDataBuilder;

public final class BlockDisplayDataBuilderImpl extends DisplayDataBuilderImpl implements BlockDisplayDataBuilder {
    private @NotNull BlockData block;

    public BlockDisplayDataBuilderImpl() {
        this.block = Bukkit.createBlockData(Material.AIR);
    }

    public BlockDisplayDataBuilderImpl(@NotNull BlockDisplayDataImpl data) {
        super(data);
        this.block = data.getBlock();
    }

    @Override
    public boolean visible() {
        return visible;
    }

    @Override
    public @NotNull BlockDisplayDataBuilderImpl visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean glowing() {
        return glowing;
    }

    @Override
    public @NotNull BlockDisplayDataBuilderImpl glowing(boolean glowing) {
        this.glowing = glowing;
        return this;
    }

    @Override
    public @NotNull BlockDisplayDataBuilder transformation(@NotNull Transformation transformation) {
        return (BlockDisplayDataBuilder) super.transformation(transformation);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder interpolationDuration(int duration) {
        return (BlockDisplayDataBuilder) super.interpolationDuration(duration);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder teleportDuration(@Range(from = 0, to = 59) int duration) {
        return (BlockDisplayDataBuilder) super.teleportDuration(duration);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder shadowRadius(float radius) {
        return (BlockDisplayDataBuilder) super.shadowRadius(radius);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder shadowStrength(float strength) {
        return (BlockDisplayDataBuilder) super.shadowStrength(strength);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder displayWidth(float width) {
        return (BlockDisplayDataBuilder) super.displayWidth(width);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder displayHeight(float height) {
        return (BlockDisplayDataBuilder) super.displayHeight(height);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder interpolationDelay(int delay) {
        return (BlockDisplayDataBuilder) super.interpolationDelay(delay);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder billboard(Display.@NotNull Billboard billboard) {
        return (BlockDisplayDataBuilder) super.billboard(billboard);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder glowColorOverride(@Nullable Color color) {
        return (BlockDisplayDataBuilder) super.glowColorOverride(color);
    }

    @Override
    public @NotNull BlockDisplayDataBuilder brightness(Display.@Nullable Brightness brightness) {
        return (BlockDisplayDataBuilder) super.brightness(brightness);
    }

    @Override
    public @NotNull BlockData blockData() {
        return this.block;
    }

    @Override
    public @NotNull BlockDisplayDataBuilder blockData(@NotNull BlockData block) {
        this.block = Check.notNull(block, "Block is null");
        return this;
    }

    @Override
    public @NotNull BlockDisplayData build() {
        return new BlockDisplayDataImpl(
                visible,
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
                brightness,
                block
        );
    }
}
