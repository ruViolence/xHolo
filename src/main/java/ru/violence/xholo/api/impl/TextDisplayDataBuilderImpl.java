package ru.violence.xholo.api.impl;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.CustomName;
import ru.violence.xholo.api.TextDisplayData;
import ru.violence.xholo.api.TextDisplayDataBuilder;

public final class TextDisplayDataBuilderImpl extends DisplayDataBuilderImpl implements TextDisplayDataBuilder {
    private @Nullable CustomName text;
    private int lineWidth;
    private @Nullable Color backgroundColor;
    private byte textOpacity;
    private boolean shadowed;
    private boolean seeThrough;
    private boolean defaultBackground;
    private TextDisplay.@NotNull TextAlignment alignment;

    public TextDisplayDataBuilderImpl() {
        this.lineWidth = 200;
        this.backgroundColor = Color.fromARGB(0x40000000);
        this.textOpacity = -1;
        this.alignment = TextDisplay.TextAlignment.CENTER;
    }

    public TextDisplayDataBuilderImpl(@NotNull TextDisplayDataImpl data) {
        super(data);
        this.text = data.getText();
        this.lineWidth = data.getLineWidth();
        this.backgroundColor = data.getBackgroundColor();
        this.textOpacity = data.getTextOpacity();
        this.shadowed = data.isShadowed();
        this.seeThrough = data.isSeeThrough();
        this.defaultBackground = data.isDefaultBackground();
        this.alignment = data.getAlignment();
    }

    @Override
    public boolean visible() {
        return visible;
    }

    @Override
    public @NotNull TextDisplayDataBuilderImpl visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean glowing() {
        return glowing;
    }

    @Override
    public @NotNull TextDisplayDataBuilderImpl glowing(boolean glowing) {
        this.glowing = glowing;
        return this;
    }

    @Override
    public @NotNull TextDisplayDataBuilder transformation(@NotNull Transformation transformation) {
        return (TextDisplayDataBuilder) super.transformation(transformation);
    }

    @Override
    public @NotNull TextDisplayDataBuilder interpolationDuration(int duration) {
        return (TextDisplayDataBuilder) super.interpolationDuration(duration);
    }

    @Override
    public @NotNull TextDisplayDataBuilder teleportDuration(@Range(from = 0, to = 59) int duration) {
        return (TextDisplayDataBuilder) super.teleportDuration(duration);
    }

    @Override
    public @NotNull TextDisplayDataBuilder shadowRadius(float radius) {
        return (TextDisplayDataBuilder) super.shadowRadius(radius);
    }

    @Override
    public @NotNull TextDisplayDataBuilder shadowStrength(float strength) {
        return (TextDisplayDataBuilder) super.shadowStrength(strength);
    }

    @Override
    public @NotNull TextDisplayDataBuilder displayWidth(float width) {
        return (TextDisplayDataBuilder) super.displayWidth(width);
    }

    @Override
    public @NotNull TextDisplayDataBuilder displayHeight(float height) {
        return (TextDisplayDataBuilder) super.displayHeight(height);
    }

    @Override
    public @NotNull TextDisplayDataBuilder interpolationDelay(int delay) {
        return (TextDisplayDataBuilder) super.interpolationDelay(delay);
    }

    @Override
    public @NotNull TextDisplayDataBuilder billboard(Display.@NotNull Billboard billboard) {
        return (TextDisplayDataBuilder) super.billboard(billboard);
    }

    @Override
    public @NotNull TextDisplayDataBuilder glowColorOverride(@Nullable Color color) {
        return (TextDisplayDataBuilder) super.glowColorOverride(color);
    }

    @Override
    public @NotNull TextDisplayDataBuilder brightness(Display.@Nullable Brightness brightness) {
        return (TextDisplayDataBuilder) super.brightness(brightness);
    }

    @Override
    public @Nullable CustomName text() {
        return text;
    }

    @Override
    public @NotNull TextDisplayDataBuilder text(@NotNull CustomName text) {
        this.text = Check.notNull(text, "Text is null");
        return this;
    }

    @Override
    public int lineWidth() {
        return lineWidth;
    }

    @Override
    public @NotNull TextDisplayDataBuilder lineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    @Override
    public @Nullable Color backgroundColor() {
        return backgroundColor;
    }

    @Override
    public @NotNull TextDisplayDataBuilder backgroundColor(@Nullable Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    public int textOpacity() {
        return textOpacity;
    }

    @Override
    public @NotNull TextDisplayDataBuilder textOpacity(byte opacity) {
        this.textOpacity = opacity;
        return this;
    }

    @Override
    public boolean shadowed() {
        return shadowed;
    }

    @Override
    public @NotNull TextDisplayDataBuilder shadowed(boolean shadowed) {
        this.shadowed = shadowed;
        return this;
    }

    @Override
    public boolean seeThrough() {
        return seeThrough;
    }

    @Override
    public @NotNull TextDisplayDataBuilder seeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
    }

    @Override
    public boolean defaultBackground() {
        return defaultBackground;
    }

    @Override
    public @NotNull TextDisplayDataBuilder defaultBackground(boolean defaultBackground) {
        this.defaultBackground = defaultBackground;
        return this;
    }

    @Override
    public TextDisplay.@NotNull TextAlignment alignment() {
        return alignment;
    }

    @Override
    public @NotNull TextDisplayDataBuilder alignment(TextDisplay.@NotNull TextAlignment alignment) {
        this.alignment = Check.notNull(alignment, "Alignment is null");
        return this;
    }

    @Override
    public @NotNull TextDisplayData build() {
        return new TextDisplayDataImpl(
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
                text,
                lineWidth,
                backgroundColor,
                textOpacity,
                shadowed,
                seeThrough,
                defaultBackground,
                alignment
        );
    }
}
