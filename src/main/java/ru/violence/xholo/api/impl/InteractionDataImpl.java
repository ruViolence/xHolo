package ru.violence.xholo.api.impl;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.InteractionData;
import ru.violence.xholo.api.InteractionDataBuilder;

@Getter
public final class InteractionDataImpl extends VirtualEntityDataImpl implements InteractionData {
    private final float width;
    private final float height;
    private final boolean responsive;

    public InteractionDataImpl(boolean visible,
                               boolean glowing,
                               float width,
                               float height,
                               boolean responsive) {
        super(visible, glowing);
        this.width = width;
        this.height = height;
        this.responsive = responsive;
    }

    @Override
    public @NotNull InteractionDataBuilder modify() {
        return new InteractionDataBuilderImpl(this);
    }
}
