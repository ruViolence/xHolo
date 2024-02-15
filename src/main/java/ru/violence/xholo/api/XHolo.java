package ru.violence.xholo.api;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.impl.VirtualArmorStandBuilderImpl;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;

@UtilityClass
public class XHolo {
    private HologramRegistryImpl registry;

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull VirtualArmorStandBuilder armorStand(@NotNull Plugin plugin) {
        return new VirtualArmorStandBuilderImpl(plugin, registry);
    }
}
