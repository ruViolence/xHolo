package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;

import java.util.function.Function;

public class FunctionName implements CustomName {
    private final @NotNull Function<Player, String> function;

    FunctionName(@NotNull Function<Player, String> function) {
        this.function = Check.notNull(function, "Function is null");
    }

    @Override
    public @Nullable String apply(@Nullable Player player) {
        return function.apply(player);
    }

    @Contract(pure = true)
    public @NotNull Function<Player, String> getFunction() {
        return function;
    }
}
