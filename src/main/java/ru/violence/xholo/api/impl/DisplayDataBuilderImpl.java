package ru.violence.xholo.api.impl;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.DisplayDataBuilder;
import ru.violence.xholo.util.UniqueInt;

public abstract class DisplayDataBuilderImpl implements DisplayDataBuilder {
    protected boolean visible;
    protected boolean glowing;
    protected @NotNull Transformation transformation;
    protected int interpolationDuration;
    protected int teleportDuration;
    protected float shadowRadius;
    protected float shadowStrength;
    protected float displayWidth;
    protected float displayHeight;
    protected @NotNull UniqueInt uniqueInterpolationDelay;
    protected @NotNull Display.Billboard billboard;
    protected @Nullable Color glowColorOverride;
    protected @Nullable Display.Brightness brightness;

    public DisplayDataBuilderImpl() {
        this.transformation = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1.0F, 1.0F, 1.0F), new Quaternionf());
        this.billboard = Display.Billboard.FIXED;
        this.shadowStrength = 1.0F;
        this.uniqueInterpolationDelay = new UniqueInt(0);
    }

    public DisplayDataBuilderImpl(@NotNull DisplayDataImpl data) {
        Check.notNull(data, "Data is null");
        this.visible = data.isVisible();
        this.glowing = data.isGlowing();
        this.transformation = data.getTransformation();
        this.interpolationDuration = data.getInterpolationDuration();
        this.teleportDuration = data.getTeleportDuration();
        this.shadowRadius = data.getShadowRadius();
        this.shadowStrength = data.getShadowStrength();
        this.displayWidth = data.getDisplayWidth();
        this.displayHeight = data.getDisplayHeight();
        this.uniqueInterpolationDelay = data.getUniqueInterpolationDelay();
        this.billboard = data.getBillboard();
        this.glowColorOverride = data.getGlowColorOverride();
        this.brightness = data.getBrightness();
    }

    @Override
    public @NotNull Transformation transformation() {
        return transformation;
    }

    @Override
    public @NotNull DisplayDataBuilder transformation(@NotNull Transformation transformation) {
        this.transformation = transformation;
        return this;
    }

    @Override
    public int interpolationDuration() {
        return interpolationDuration;
    }

    @Override
    public @NotNull DisplayDataBuilder interpolationDuration(int duration) {
        this.interpolationDuration = duration;
        return this;
    }

    @Override
    public @Range(from = 0, to = 59) int teleportDuration() {
        return teleportDuration;
    }

    @Override
    public @NotNull DisplayDataBuilder teleportDuration(@Range(from = 0, to = 59) int duration) {
        this.teleportDuration = duration;
        return this;
    }

    @Override
    public float shadowRadius() {
        return shadowRadius;
    }

    @Override
    public @NotNull DisplayDataBuilder shadowRadius(float radius) {
        this.shadowRadius = radius;
        return this;
    }

    @Override
    public float shadowStrength() {
        return shadowStrength;
    }

    @Override
    public @NotNull DisplayDataBuilder shadowStrength(float strength) {
        this.shadowStrength = strength;
        return this;
    }

    @Override
    public float displayWidth() {
        return displayWidth;
    }

    @Override
    public @NotNull DisplayDataBuilder displayWidth(float width) {
        this.displayWidth = width;
        return this;
    }

    @Override
    public float displayHeight() {
        return displayHeight;
    }

    @Override
    public @NotNull DisplayDataBuilder displayHeight(float height) {
        this.displayHeight = height;
        return this;
    }

    @Override
    public int interpolationDelay() {
        return uniqueInterpolationDelay.get();
    }

    @Override
    public @NotNull DisplayDataBuilder interpolationDelay(int delay) {
        this.uniqueInterpolationDelay = uniqueInterpolationDelay.set(delay);
        return this;
    }

    @Override
    public @NotNull Display.Billboard billboard() {
        return billboard;
    }

    @Override
    public @NotNull DisplayDataBuilder billboard(Display.@NotNull Billboard billboard) {
        this.billboard = Check.notNull(billboard, "Billboard is null");
        return this;
    }

    @Override
    public @Nullable Color glowColorOverride() {
        return glowColorOverride;
    }

    @Override
    public @NotNull DisplayDataBuilder glowColorOverride(@Nullable Color color) {
        this.glowColorOverride = color;
        return this;
    }

    @Override
    public @Nullable Display.Brightness brightness() {
        return brightness;
    }

    @Override
    public @NotNull DisplayDataBuilder brightness(Display.@Nullable Brightness brightness) {
        this.brightness = brightness;
        return this;
    }
}
