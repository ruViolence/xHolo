package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.bukkit.api.util.MessageUtil;
import ru.violence.coreapi.common.message.LegacyPrinter;
import ru.violence.coreapi.common.message.MessageKey;
import ru.violence.coreapi.common.util.Check;

public final class KeyedName implements CustomName {
    private final @NotNull MessageKey key;

    KeyedName(@NotNull MessageKey key) {
        this.key = Check.notNull(key, "Key is null");
    }

    @Override
    @Contract(pure = true)
    public @NotNull String apply(@Nullable Player player) {
        return player != null ? MessageUtil.renderLegacy(player, key) : LegacyPrinter.print(key);
    }

    @Contract(pure = true)
    public @NotNull MessageKey getKey() {
        return key;
    }
}
