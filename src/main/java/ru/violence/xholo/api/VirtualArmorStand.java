package ru.violence.xholo.api;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VirtualArmorStand extends VirtualEntity {
    @Contract(pure = true)
    @NotNull ArmorStandData getData();

    void setData(@NotNull ArmorStandData data);

    @Contract(pure = true)
    @Nullable ItemStack getItemInHand();

    void setItemInHand(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack getItemInOffHand();

    void setItemInOffHand(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack getBoots();

    void setBoots(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack getLeggings();

    void setLeggings(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack getChestplate();

    void setChestplate(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack getHelmet();

    void setHelmet(@Nullable ItemStack item);
}
