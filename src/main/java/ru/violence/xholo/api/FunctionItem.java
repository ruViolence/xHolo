package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;

import java.util.function.Function;

public class FunctionItem implements CustomItem {
    private final @NotNull Function<@Nullable Player, @Nullable ItemStack> function;

    FunctionItem(@NotNull Function<@Nullable Player, @Nullable ItemStack> function) {
        this.function = Check.notNull(function, "Function is null");
    }

    @Override
    public @Nullable ItemStack apply(@Nullable Player player) {
        return function.apply(player);
    }

    @Contract(pure = true)
    public @NotNull Function<@Nullable Player, @Nullable ItemStack> getFunction() {
        return function;
    }
}
