package ru.violence.xholo.api.impl;

import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.InteractionData;
import ru.violence.xholo.api.InteractionDataBuilder;

public final class InteractionDataBuilderImpl implements InteractionDataBuilder {
    private float width;
    private float height;
    private boolean responsive;

    public InteractionDataBuilderImpl() {
        this.width = 1.0F;
        this.height = 1.0F;
        this.responsive = false;
    }

    public InteractionDataBuilderImpl(@NotNull InteractionDataImpl data) {
        this.width = data.getWidth();
        this.height = data.getHeight();
        this.responsive = data.isResponsive();
    }

    @Override
    public float width() {
        return width;
    }

    @Override
    public @NotNull InteractionDataBuilder width(float width) {
        this.width = width;
        return this;
    }

    @Override
    public float height() {
        return height;
    }

    @Override

    public @NotNull InteractionDataBuilder height(float height) {
        this.height = height;
        return this;
    }

    @Override
    public boolean responsive() {
        return responsive;
    }

    @Override
    public @NotNull InteractionDataBuilder responsive(boolean responsive) {
        this.responsive = responsive;
        return this;
    }

    @Override
    public @NotNull InteractionData build() {
        return new InteractionDataImpl(
                true,
                false,
                width,
                height,
                responsive
        );
    }
}
