package ru.violence.xholo.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.impl.InteractionDataBuilderImpl;
import ru.violence.xholo.api.impl.InteractionDataImpl;

public interface InteractionDataBuilder {
    @Contract(value = "-> new", pure = true)
    static @NotNull InteractionDataBuilder builder() {
        return new InteractionDataBuilderImpl();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull InteractionDataBuilder from(@NotNull InteractionData data) {
        return new InteractionDataBuilderImpl((InteractionDataImpl) data);
    }

    @Contract(pure = true)
    float width();

    @Contract(value = "_ -> this")
    @NotNull InteractionDataBuilder width(float width);

    @Contract(pure = true)
    float height();

    @Contract(value = "_ -> this")
    @NotNull InteractionDataBuilder height(float height);

    @Contract(pure = true)
    boolean responsive();

    @Contract(value = "_ -> this")
    @NotNull InteractionDataBuilder responsive(boolean responsive);

    @Contract(pure = true)
    @NotNull InteractionData build();
}
