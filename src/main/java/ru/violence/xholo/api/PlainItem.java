package ru.violence.xholo.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class PlainItem implements CustomItem {
    private final @Nullable ItemStack item;

    PlainItem(@Nullable ItemStack item) {
        this.item = item;
    }

    @Override
    @Contract(pure = true)
    public @Nullable ItemStack apply(@Nullable Player player) {
        return item;
    }

    @Contract(pure = true)
    public @Nullable ItemStack getItem() {
        return item;
    }
}
