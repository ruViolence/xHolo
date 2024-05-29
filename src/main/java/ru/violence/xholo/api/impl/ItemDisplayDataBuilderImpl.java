package ru.violence.xholo.api.impl;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.CustomItem;
import ru.violence.xholo.api.ItemDisplayData;
import ru.violence.xholo.api.ItemDisplayDataBuilder;

public final class ItemDisplayDataBuilderImpl extends DisplayDataBuilderImpl implements ItemDisplayDataBuilder {
    private @Nullable CustomItem item;
    private ItemDisplay.@NotNull ItemDisplayTransform displayTransform;

    public ItemDisplayDataBuilderImpl() {
        this.displayTransform = ItemDisplay.ItemDisplayTransform.NONE;
    }

    public ItemDisplayDataBuilderImpl(@NotNull ItemDisplayDataImpl data) {
        super(data);
        this.item = data.getItem();
        this.displayTransform = data.getDisplayTransform();
    }

    @Override
    public boolean visible() {
        return visible;
    }

    @Override
    public @NotNull ItemDisplayDataBuilderImpl visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean glowing() {
        return glowing;
    }

    @Override
    public @NotNull ItemDisplayDataBuilderImpl glowing(boolean glowing) {
        this.glowing = glowing;
        return this;
    }

    @Override
    public @NotNull ItemDisplayDataBuilder transformation(@NotNull Transformation transformation) {
        return (ItemDisplayDataBuilder) super.transformation(transformation);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder interpolationDuration(int duration) {
        return (ItemDisplayDataBuilder) super.interpolationDuration(duration);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder teleportDuration(@Range(from = 0, to = 59) int duration) {
        return (ItemDisplayDataBuilder) super.teleportDuration(duration);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder shadowRadius(float radius) {
        return (ItemDisplayDataBuilder) super.shadowRadius(radius);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder shadowStrength(float strength) {
        return (ItemDisplayDataBuilder) super.shadowStrength(strength);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder displayWidth(float width) {
        return (ItemDisplayDataBuilder) super.displayWidth(width);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder displayHeight(float height) {
        return (ItemDisplayDataBuilder) super.displayHeight(height);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder interpolationDelay(int delay) {
        return (ItemDisplayDataBuilder) super.interpolationDelay(delay);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder billboard(Display.@NotNull Billboard billboard) {
        return (ItemDisplayDataBuilder) super.billboard(billboard);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder glowColorOverride(@Nullable Color color) {
        return (ItemDisplayDataBuilder) super.glowColorOverride(color);
    }

    @Override
    public @NotNull ItemDisplayDataBuilder brightness(Display.@Nullable Brightness brightness) {
        return (ItemDisplayDataBuilder) super.brightness(brightness);
    }

    @Override
    public @Nullable CustomItem item() {
        return this.item;
    }

    @Override
    public @NotNull ItemDisplayDataBuilder item(@Nullable CustomItem item) {
        this.item = item;
        return this;
    }

    @Override
    public ItemDisplay.@NotNull ItemDisplayTransform displayTransform() {
        return this.displayTransform;
    }

    @Override
    public @NotNull ItemDisplayDataBuilder displayTransform(ItemDisplay.@NotNull ItemDisplayTransform displayTransform) {
        this.displayTransform = Check.notNull(displayTransform, "ItemDisplayTransform is null");
        return this;
    }

    @Override
    public @NotNull ItemDisplayData build() {
        return new ItemDisplayDataImpl(
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
                item,
                displayTransform
        );
    }
}
