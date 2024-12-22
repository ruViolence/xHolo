package ru.violence.xholo.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.VirtualEntity;
import ru.violence.xholo.api.XHolo;

import java.util.List;

@UtilityClass
public class RegistryHelper {
    public static @NotNull List<VirtualEntity> getVirtualPassengers(@NotNull Player viewer, org.bukkit.entity.@NotNull Entity vehicle) {
        return XHolo.getRegistry().getAllVisibleFor(viewer)
                .stream()
                .filter(ve -> ve.manager().getVehicle() == vehicle)
                .toList();
    }

    public static @NotNull List<VirtualEntity> getOtherVirtualPassengers(@NotNull Player viewer, int excludeId, org.bukkit.entity.@NotNull Entity vehicle) {
        return XHolo.getRegistry().getAllVisibleFor(viewer)
                .stream()
                .filter(ve -> ve.manager().getVehicle() == vehicle && ve.getEntityId() != excludeId)
                .toList();
    }
}
