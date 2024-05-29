package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public interface CustomItem extends Function<Player, ItemStack>, Supplier<ItemStack> {
    @Contract(value = "_ -> new", pure = true)
    static @NotNull PlainItem plain(@Nullable ItemStack component) {
        return new PlainItem(component);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull FunctionItem dynamic(@NotNull Function<@Nullable Player, @Nullable ItemStack> func) {
        return new FunctionItem(func);
    }

    @Override
    @Contract(pure = true)
    @Nullable
    ItemStack apply(@Nullable Player player);

    @Override
    @Contract(pure = true)
    default @Nullable ItemStack get() {
        return apply(null);
    }
}
