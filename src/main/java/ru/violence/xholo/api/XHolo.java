package ru.violence.xholo.api;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.impl.VirtualArmorStandBuilderImpl;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;

@UtilityClass
public class XHolo {
    private HologramRegistryImpl registry;

    @Deprecated
    public static VirtualArmorStandBuilder builder() {
        return new VirtualArmorStandBuilderImpl(null, registry);
    }

    public static VirtualArmorStandBuilder builder(@NotNull Plugin plugin) {
        return new VirtualArmorStandBuilderImpl(Check.notNull(plugin), registry);
    }
}
