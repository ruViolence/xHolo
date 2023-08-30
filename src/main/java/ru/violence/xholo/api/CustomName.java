package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.message.MessageKey;

import java.util.function.Function;
import java.util.function.Supplier;

public interface CustomName extends Function<Player, String>, Supplier<String> {
    @Contract(value = "_ -> new", pure = true)
    static @NotNull StringName text(@NotNull String text) {
        return new StringName(text);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull KeyedName key(@NotNull MessageKey key) {
        return new KeyedName(key);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull FunctionName func(@NotNull Function<Player, String> function) {
        return new FunctionName(function);
    }

    @Override
    @Contract(pure = true)
    @Nullable String apply(@Nullable Player player);

    @Override
    @Contract(pure = true)
    default @Nullable String get() {
        return apply(null);
    }
}
