package ru.violence.xholo.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.xholo.api.VirtualInteraction;

public class AsyncVirtualInteractionEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NotNull VirtualInteraction entity;
    private final boolean sneaking;
    private final boolean attack;
    private final @Nullable Vector clickedPosition;
    private final @NotNull EquipmentSlot hand;

    public AsyncVirtualInteractionEvent(@NotNull Player who, @NotNull VirtualInteraction entity, boolean isSneaking, boolean attack, @Nullable Vector clickedPosition, @NotNull EquipmentSlot hand) {
        super(who, true);
        this.entity = entity;
        this.sneaking = isSneaking;
        this.attack = attack;
        this.clickedPosition = clickedPosition;
        this.hand = hand;
    }

    @Contract(pure = true)
    public @NotNull VirtualInteraction getEntity() {
        return this.entity;
    }

    @Contract(pure = true)
    public boolean isSneaking() {
        return this.sneaking;
    }

    @Contract(pure = true)
    public boolean isAttack() {
        return this.attack;
    }

    @Contract(pure = true)
    public @Nullable Vector getClickedRelativePosition() {
        return this.clickedPosition;
    }

    @Contract(pure = true)
    public @NotNull EquipmentSlot getHand() {
        return hand;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
