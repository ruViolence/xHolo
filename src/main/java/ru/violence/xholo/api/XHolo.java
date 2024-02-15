package ru.violence.xholo.api;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.impl.VirtualArmorStandBuilderImpl;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;

@UtilityClass
public class XHolo {
    private HologramRegistryImpl registry;

    public static VirtualArmorStandBuilder builder(@NotNull Plugin plugin) {
        return new VirtualArmorStandBuilderImpl(plugin, registry);
    }
}
