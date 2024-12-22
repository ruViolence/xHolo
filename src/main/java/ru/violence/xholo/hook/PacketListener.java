package ru.violence.xholo.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.VirtualEntity;
import ru.violence.xholo.api.VirtualInteraction;
import ru.violence.xholo.api.event.AsyncVirtualInteractionEvent;

import java.util.Arrays;
import java.util.List;

public class PacketListener extends PacketAdapter {
    private final XHoloPlugin holoPlugin;

    public PacketListener(XHoloPlugin holoPlugin) {
        super(holoPlugin,
                ListenerPriority.NORMAL,
                List.of(
                        PacketType.Play.Client.USE_ENTITY,
                        PacketType.Play.Server.MOUNT
                ),
                ListenerOptions.ASYNC);
        this.holoPlugin = holoPlugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.isCancelled()) return;
        if (event.isPlayerTemporary()) return;

        int entityId = event.getPacket().getIntegers().read(0);

        VirtualEntity ve = holoPlugin.getRegistry().getFromId(entityId);
        if (ve == null) return;

        Player player = event.getPlayer();
        if (!ve.manager().isVisibleFor(player)) return;

        event.setCancelled(true);

        if (ve instanceof VirtualInteraction vi) {
            WrappedEnumEntityUseAction useAction = event.getPacket().getEnumEntityUseActions().read(0);
            Boolean isSneaking = event.getPacket().getBooleans().read(0);

            EnumWrappers.EntityUseAction action = useAction.getAction();

            boolean isInteractAt = action == EnumWrappers.EntityUseAction.INTERACT_AT;
            boolean isAttack = action == EnumWrappers.EntityUseAction.ATTACK;
            Vector clickedPosition = isInteractAt ? useAction.getPosition() : null;

            EquipmentSlot hand = resolveHand(isAttack, isInteractAt, clickedPosition != null, action == EnumWrappers.EntityUseAction.INTERACT);

            Bukkit.getScheduler().runTaskAsynchronously(holoPlugin, () -> Bukkit.getPluginManager().callEvent(new AsyncVirtualInteractionEvent(player, vi, isSneaking, isAttack, clickedPosition, hand)));
        }

        // TODO: Add ArmorStand interaction event
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.isCancelled()) return;
        if (event.isPlayerTemporary()) return;

        int vehicleId = event.getPacket().getIntegers().read(0);

        Player player = event.getPlayer();

        List<VirtualEntity> virtualPassengers = holoPlugin.getRegistry().getAllVisibleFor(player)
                .stream()
                .filter(virtualEntity -> {
                    Entity vehicle = virtualEntity.manager().getVehicle();
                    return vehicle != null && vehicle.getEntityId() == vehicleId;
                })
                .filter(virtualEntity -> virtualEntity.manager().isShown(player))
                .toList();

        if (virtualPassengers.isEmpty()) return;

        int[] passengerIds = event.getPacket().getIntegerArrays().read(0);

        for (VirtualEntity ve : virtualPassengers) {
            // Vehicle does not have real passengers, so we don't need to modify packet
            if (Arrays.stream(passengerIds).anyMatch(id -> id == ve.getEntityId())) return;
        }

        int[] newPassengerIds = Arrays.copyOf(passengerIds, passengerIds.length + virtualPassengers.size());
        for (int i = 0; i < virtualPassengers.size(); i++)
            newPassengerIds[i + passengerIds.length] = virtualPassengers.get(i).getEntityId();

        event.getPacket().getIntegerArrays().write(0, newPassengerIds);
    }

    private @NotNull EquipmentSlot resolveHand(boolean isAttack, boolean isInteractAt, boolean hasClickedPosition, boolean isInteract) {
        // Attack action, so it's main hand
        if (isAttack) return EquipmentSlot.HAND;
        // Main hand interaction
        if (isInteractAt) return hasClickedPosition ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
        // Offhand interaction
        if (isInteract) return EquipmentSlot.OFF_HAND;
        // By default, main hand
        return EquipmentSlot.HAND;
    }
}
