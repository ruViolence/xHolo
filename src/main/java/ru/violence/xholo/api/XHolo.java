package ru.violence.xholo.api;

import lombok.experimental.UtilityClass;
import ru.violence.xholo.api.impl.VirtualArmorStandBuilderImpl;
import ru.violence.xholo.api.registry.impl.HologramRegistryImpl;

@UtilityClass
public class XHolo {
    private HologramRegistryImpl registry;

    public static VirtualArmorStandBuilder builder() {
        return new VirtualArmorStandBuilderImpl(registry);
    }
}
