package ru.violence.xholo.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.message.MessageKey;

import java.util.function.Function;
import java.util.function.Supplier;

public interface CustomName extends Function<Player, Component>, Supplier<Component> {
    @Contract(value = "_ -> new", pure = true)
    static @NotNull ComponentName component(@NotNull Component component) {
        return new ComponentName(component);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull KeyedName key(@NotNull MessageKey key) {
        return new KeyedName(key);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull FunctionName func(@NotNull Function<@Nullable Player, @Nullable Component> function) {
        return new FunctionName(function);
    }

    @Override
    @Contract(pure = true)
    @Nullable Component apply(@Nullable Player player);

    @Override
    @Contract(pure = true)
    default @Nullable Component get() {
        return apply(null);
    }
}
