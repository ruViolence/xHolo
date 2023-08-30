package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.bukkit.api.util.RendererHelper;
import ru.violence.coreapi.common.api.message.MessageKey;
import ru.violence.coreapi.common.api.message.Renderer;
import ru.violence.coreapi.common.api.util.Check;

public final class KeyedName implements CustomName {
    private final @NotNull MessageKey key;

    KeyedName(@NotNull MessageKey key) {
        this.key = Check.notNull(key, "Key is null");
    }

    @Override
    @Contract(pure = true)
    public @NotNull String apply(@Nullable Player player) {
        return player != null ? RendererHelper.legacy(player, key) : Renderer.legacy(key);
    }

    @Contract(pure = true)
    public @NotNull MessageKey getKey() {
        return key;
    }
}
