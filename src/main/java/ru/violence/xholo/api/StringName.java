package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;

public class StringName implements CustomName {
    private final @NotNull String name;

    StringName(@NotNull String name) {
        this.name = Check.notNull(name, "String is null");
    }

    @Override
    @Contract(pure = true)
    public @NotNull String apply(@Nullable Player player) {
        return name;
    }

    @Contract(pure = true)
    public @NotNull String getName() {
        return name;
    }
}
