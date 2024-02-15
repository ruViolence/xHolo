package ru.violence.xholo.util.nms;

import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;
import net.minecraft.core.Rotations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.reflection.ReflectMethod;
import ru.violence.coreapi.common.api.reflection.ReflectionUtil;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.util.UpdateFlag;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class NMSUtil {
    private final AtomicInteger ENTITY_COUNTER = ReflectionUtil.getFieldValue(Entity.class, null, "d");

    private final EntityDataAccessor<Byte> DP_ENTITY_SHARED_FLAGS = ReflectionUtil.getFieldValue(Entity.class, null, "ao");
    private final EntityDataAccessor<Optional<Component>> DP_ENTITY_CUSTOM_NAME = ReflectionUtil.getFieldValue(Entity.class, null, "aU");
    private final EntityDataAccessor<Boolean> DP_ENTITY_CUSTOM_NAME_VISIBLE = ReflectionUtil.getFieldValue(Entity.class, null, "aV");

    private final EntityDataAccessor<Byte> DP_ARMOR_STAND_CLIENT_FLAGS = ArmorStand.DATA_CLIENT_FLAGS;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_HEAD_POSE = ArmorStand.DATA_HEAD_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_BODY_POSE = ArmorStand.DATA_BODY_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_LEFT_ARM_POSE = ArmorStand.DATA_LEFT_ARM_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_RIGHT_ARM_POSE = ArmorStand.DATA_RIGHT_ARM_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_LEFT_LEG_POSE = ArmorStand.DATA_LEFT_LEG_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_RIGHT_LEG_POSE = ArmorStand.DATA_RIGHT_LEG_POSE;

    private final ReflectMethod<Integer> METHOD_TRACKEDENTITY_GETEFFECTIVERANGE = new ReflectMethod<>(ChunkMap.TrackedEntity.class, "b", (Class<?>[]) null);

    public boolean isRealPlayer(@NotNull Player player) {
        if (player instanceof CraftPlayer) {
            ServerPlayer handle = ((CraftPlayer) player).getHandle();
            return handle != null && handle.connection != null;
        }
        return false;
    }

    public void spawnEntityArmorStand(@NotNull Player player, int entityId, @NotNull Location location, @NotNull ArmorStandData data,
                                      @Nullable ItemStack mainHandItem,
                                      @Nullable ItemStack offHandItem,
                                      @Nullable ItemStack headItem,
                                      @Nullable ItemStack chestItem,
                                      @Nullable ItemStack legsItem,
                                      @Nullable ItemStack feetItem) {
        spawnEntityLiving(player, location, entityId);
        updateMetadata(player, entityId, data, null);

        sendEquipment(player, entityId, new Map.Entry[]{
                new AbstractMap.SimpleEntry<>(EquipmentSlot.HAND, mainHandItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.OFF_HAND, offHandItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.HEAD, headItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.CHEST, chestItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.LEGS, legsItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.FEET, feetItem)
        }, true);
    }

    public void spawnEntityLiving(@NotNull Player player, @NotNull Location location, int entityId) {
        ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(
                entityId,
                UUID.randomUUID(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw(),
                EntityType.ARMOR_STAND,
                0,
                Vec3.ZERO,
                location.getYaw()
        );

        sendPacket(player, packet);
    }

    public void sendEquipment(@NotNull Player player, int entityId,
                              @Nullable Map.Entry<EquipmentSlot, ItemStack>[] equipment,
                              boolean isOnSpawn) {
        if (equipment == null || equipment.length == 0) return;

        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>(equipment.length);

        for (Map.Entry<EquipmentSlot, ItemStack> pair : equipment) {
            if (isOnSpawn && (pair.getValue() == null || pair.getValue().getType() == Material.AIR)) continue;
            equipmentList.add(Pair.of(toNMS(pair.getKey()), CraftItemStack.asNMSCopy(pair.getValue())));
        }

        if (!equipmentList.isEmpty()) {
            sendPacket(player, new ClientboundSetEquipmentPacket(entityId, equipmentList));
        }
    }

    public void updateMetadata(@NotNull Player player, int entityId, @NotNull ArmorStandData data, UpdateFlag @Nullable [] flags) {
        SynchedEntityData watcher = flags == null || flags.length == 0
                ? createDataWatcher(player, data)
                : createDataWatcher(player, data, flags);

        List<SynchedEntityData.DataValue<?>> dataValues = watcher.packDirty();
        sendPacket(player, new ClientboundSetEntityDataPacket(entityId, dataValues));
    }

    public void teleportEntity(@NotNull Player player, int entityId, @NotNull Location location) {
        FriendlyByteBuf fbb = new FriendlyByteBuf(Unpooled.buffer());

        fbb.writeVarInt(entityId);
        fbb.writeDouble(location.getX());
        fbb.writeDouble(location.getY());
        fbb.writeDouble(location.getZ());
        fbb.writeByte((byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        fbb.writeByte((byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        fbb.writeBoolean(true); // onGround

        sendPacket(player, new ClientboundTeleportEntityPacket(fbb));
    }

    public void destroyEntities(@NotNull Player player, int @NotNull ... entityIds) {
        sendPacket(player, new ClientboundRemoveEntitiesPacket(entityIds));
    }

    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        Check.isTrue(Packet.class.isAssignableFrom(packet.getClass()), "Object is not a packet");
        ((CraftPlayer) player).getHandle().connection.send((Packet<?>) packet);
    }

    @Contract(pure = true)
    public int getNextEntityId() {
        return ENTITY_COUNTER.incrementAndGet();
    }

    @Contract(pure = true)
    private @NotNull SynchedEntityData createDataWatcher(@NotNull Player player, @NotNull ArmorStandData data) {
        SynchedEntityData watcher = new SynchedEntityData(null);

        // Set entity flags
        setDWEntityFlags(watcher, !data.isVisible());

        // Set custom name
        setDWCustomName(watcher, data.getCustomName() != null ? data.getCustomName().apply(player) : null);

        // Set ArmorStand status
        setDWArmorStandClientFlags(watcher, data.isSmall(), data.isHasArms(), !data.isHasBasePlate(), data.isMarker());

        // Set rotations
        if (data.getHeadPose() != null) setDWArmorStandHeadPose(watcher, data.getHeadPose());
        if (data.getBodyPose() != null) setDWArmorStandBodyPose(watcher, data.getBodyPose());
        if (data.getLeftArmPose() != null) setDWArmorStandLeftArmPose(watcher, data.getLeftArmPose());
        if (data.getRightArmPose() != null) setDWArmorStandRightArmPose(watcher, data.getRightArmPose());
        if (data.getLeftLegPose() != null) setDWArmorStandLeftLegPose(watcher, data.getLeftLegPose());
        if (data.getRightLegPose() != null) setDWArmorStandRightLegPose(watcher, data.getRightLegPose());

        return watcher;
    }

    @Contract(pure = true)
    public @NotNull SynchedEntityData createDataWatcher(@NotNull Player player, @NotNull ArmorStandData data, UpdateFlag @Nullable [] flags) {
        if (flags == null || flags.length == 0) return createDataWatcher(player, data);

        SynchedEntityData watcher = new SynchedEntityData(null);

        for (UpdateFlag flag : flags) {
            switch (flag) {
                case STATUS:
                    setDWArmorStandClientFlags(watcher, data.isSmall(), data.isHasArms(), !data.isHasBasePlate(), data.isMarker());
                    break;
                case FLAGS:
                    setDWEntityFlags(watcher, !data.isVisible());
                    break;
                case CUSTOM_NAME:
                    setDWCustomName(watcher, data.getCustomName() != null ? data.getCustomName().apply(player) : null);
                    break;
                case BODY_POSE:
                    if (data.getBodyPose() != null) setDWArmorStandBodyPose(watcher, data.getBodyPose());
                    break;
                case LEFT_ARM_POSE:
                    if (data.getLeftArmPose() != null) setDWArmorStandLeftArmPose(watcher, data.getLeftArmPose());
                    break;
                case RIGHT_ARM_POSE:
                    if (data.getRightArmPose() != null) setDWArmorStandRightArmPose(watcher, data.getRightArmPose());
                    break;
                case LEFT_LEG_POSE:
                    if (data.getLeftLegPose() != null) setDWArmorStandLeftLegPose(watcher, data.getLeftLegPose());
                    break;
                case RIGHT_LEG_POSE:
                    if (data.getRightLegPose() != null) setDWArmorStandRightLegPose(watcher, data.getRightLegPose());
                    break;
                case HEAD_POSE:
                    if (data.getHeadPose() != null) setDWArmorStandHeadPose(watcher, data.getHeadPose());
                    break;
            }
        }

        return watcher;
    }

    @Contract(pure = true)
    private @NotNull Rotations toNMS(@NotNull EulerAngle old) {
        return new Rotations(
                (float) Math.toDegrees(old.getX()),
                (float) Math.toDegrees(old.getY()),
                (float) Math.toDegrees(old.getZ())
        );
    }

    @Contract(pure = true)
    private @NotNull net.minecraft.world.entity.EquipmentSlot toNMS(@NotNull EquipmentSlot old) {
        switch (old) {
            case HAND:
                return net.minecraft.world.entity.EquipmentSlot.MAINHAND;
            case OFF_HAND:
                return net.minecraft.world.entity.EquipmentSlot.OFFHAND;
            case FEET:
                return net.minecraft.world.entity.EquipmentSlot.FEET;
            case LEGS:
                return net.minecraft.world.entity.EquipmentSlot.LEGS;
            case CHEST:
                return net.minecraft.world.entity.EquipmentSlot.CHEST;
            case HEAD:
                return net.minecraft.world.entity.EquipmentSlot.HEAD;
            default:
                throw new IllegalStateException("Unexpected value: " + old);
        }
    }

    @Contract(pure = true)
    private byte setFlag(byte old, int bit, boolean flag) {
        if (flag) {
            return (byte) (old | 1 << bit);
        } else {
            return (byte) (old & ~(1 << bit));
        }
    }

    @Contract(pure = true)
    private byte setStatus(byte old, int bit, boolean flag) {
        if (flag) {
            return (byte) (old | bit);
        } else {
            return (byte) (old & ~bit);
        }
    }

    public int getFurthestViewableBlock(@NotNull Player player) {
        return METHOD_TRACKEDENTITY_GETEFFECTIVERANGE.invoke(((CraftPlayer) player).getHandle().tracker);
    }

    private void setDWEntityFlags(@NotNull SynchedEntityData watcher, boolean invisible) {
        byte flags = 0;
        flags = setFlag(flags, 0x5, invisible);
        defineDataValue(watcher, DP_ENTITY_SHARED_FLAGS, flags);
    }

    private void setDWCustomName(@NotNull SynchedEntityData watcher, @Nullable net.kyori.adventure.text.Component customName) {
        Component nmsName = customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null;
        defineDataValue(watcher, DP_ENTITY_CUSTOM_NAME, Optional.ofNullable(nmsName));
        defineDataValue(watcher, DP_ENTITY_CUSTOM_NAME_VISIBLE, nmsName != null);
    }

    private void setDWArmorStandClientFlags(@NotNull SynchedEntityData watcher, boolean small, boolean hasArms, boolean noBasePlate, boolean marker) {
        byte status = 0;
        status = setStatus(status, 0x1, small);
        status = setStatus(status, 0x4, hasArms);
        status = setStatus(status, 0x8, noBasePlate);
        status = setStatus(status, 0x10, marker);
        defineDataValue(watcher, DP_ARMOR_STAND_CLIENT_FLAGS, status);
    }

    private void setDWArmorStandHeadPose(@NotNull SynchedEntityData watcher, @NotNull EulerAngle pose) {
        defineDataValue(watcher, DP_ARMOR_STAND_HEAD_POSE, toNMS(pose));
    }

    private void setDWArmorStandBodyPose(@NotNull SynchedEntityData watcher, @NotNull EulerAngle pose) {
        defineDataValue(watcher, DP_ARMOR_STAND_BODY_POSE, toNMS(pose));
    }

    private void setDWArmorStandLeftArmPose(@NotNull SynchedEntityData watcher, @NotNull EulerAngle pose) {
        defineDataValue(watcher, DP_ARMOR_STAND_LEFT_ARM_POSE, toNMS(pose));
    }

    private void setDWArmorStandRightArmPose(@NotNull SynchedEntityData watcher, @NotNull EulerAngle pose) {
        defineDataValue(watcher, DP_ARMOR_STAND_RIGHT_ARM_POSE, toNMS(pose));
    }

    private void setDWArmorStandLeftLegPose(@NotNull SynchedEntityData watcher, @NotNull EulerAngle pose) {
        defineDataValue(watcher, DP_ARMOR_STAND_LEFT_LEG_POSE, toNMS(pose));
    }

    private void setDWArmorStandRightLegPose(@NotNull SynchedEntityData watcher, @NotNull EulerAngle pose) {
        defineDataValue(watcher, DP_ARMOR_STAND_RIGHT_LEG_POSE, toNMS(pose));
    }

    private <T> void defineDataValue(@NotNull SynchedEntityData watcher, @NotNull EntityDataAccessor<T> dataValue, T value) {
        watcher.define(dataValue, value);
        watcher.markDirty(dataValue);
    }
}
