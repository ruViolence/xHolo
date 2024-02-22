package ru.violence.xholo.api.impl;

import lombok.Getter;

@Getter
public abstract class VirtualEntityDataImpl {
    private final boolean visible;
    private final boolean glowing;

    public VirtualEntityDataImpl(boolean visible, boolean glowing) {
        this.visible = visible;
        this.glowing = glowing;
    }
}
