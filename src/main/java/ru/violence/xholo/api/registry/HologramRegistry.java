package ru.violence.xholo.api.registry;

import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.VirtualArmorStand;

import java.util.List;

public interface HologramRegistry {
    @Contract(value = "_ -> new", pure = true)
    @NotNull List<VirtualArmorStand> getAllFrom(@NotNull World world);

    @Contract(value = "-> new", pure = true)
    @NotNull List<VirtualArmorStand> getAll();
}
