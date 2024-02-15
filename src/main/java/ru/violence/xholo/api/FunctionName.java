package ru.violence.xholo.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;

import java.util.function.Function;

public class FunctionName implements CustomName {
    private final @NotNull Function<@Nullable Player, @Nullable Component> function;

    FunctionName(@NotNull Function<@Nullable Player, @Nullable Component> function) {
        this.function = Check.notNull(function, "Function is null");
    }

    @Override
    public @Nullable Component apply(@Nullable Player player) {
        return function.apply(player);
    }

    @Contract(pure = true)
    public @NotNull Function<@Nullable Player, @Nullable Component> getFunction() {
        return function;
    }
}
