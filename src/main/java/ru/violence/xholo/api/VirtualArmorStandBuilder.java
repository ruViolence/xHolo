package ru.violence.xholo.api;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VirtualArmorStandBuilder extends VirtualEntityBuilder {
    @Override
    @NotNull VirtualArmorStandBuilder location(@NotNull Location location);

    @Contract(pure = true)
    @Nullable ArmorStandData data();

    @Contract(value = "_ -> this")
    @NotNull VirtualArmorStandBuilder data(@NotNull ArmorStandData data);

    @Contract(pure = true)
    @Nullable ItemStack itemInHand();

    @Contract(value = "_ -> this")
    @NotNull VirtualArmorStandBuilder itemInHand(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack itemInOffHand();

    @Contract(value = "_ -> this")
    @NotNull VirtualArmorStandBuilder itemInOffHand(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack boots();

    @Contract(value = "_ -> this")
    @NotNull VirtualArmorStandBuilder boots(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack leggings();

    @Contract(value = "_ -> this")
    @NotNull VirtualArmorStandBuilder leggings(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack chestplate();

    @Contract(value = "_ -> this")
    @NotNull VirtualArmorStandBuilder chestplate(@Nullable ItemStack item);

    @Contract(pure = true)
    @Nullable ItemStack helmet();

    @Contract(value = "_ -> this")
    @NotNull VirtualArmorStandBuilder helmet(@Nullable ItemStack item);

    @Contract(value = "-> new", pure = true)
    @NotNull VirtualArmorStand build();
}
