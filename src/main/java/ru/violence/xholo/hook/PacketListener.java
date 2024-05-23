package ru.violence.xholo.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.VirtualEntity;
import ru.violence.xholo.api.VirtualInteraction;
import ru.violence.xholo.api.event.AsyncVirtualInteractionEvent;

import java.util.Collections;

public class PacketListener extends PacketAdapter {
    private final XHoloPlugin holoPlugin;

    public PacketListener(XHoloPlugin holoPlugin) {
        super(holoPlugin, ListenerPriority.NORMAL, Collections.singleton(PacketType.Play.Client.USE_ENTITY), ListenerOptions.ASYNC);
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
