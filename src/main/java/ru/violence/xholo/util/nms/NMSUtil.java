package ru.violence.xholo.util.nms;

import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import io.papermc.paper.adventure.PaperAdventure;
import lombok.experimental.UtilityClass;
import net.minecraft.core.Rotations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import ru.violence.coreapi.common.api.reflection.ReflectionUtil;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.BlockDisplayData;
import ru.violence.xholo.api.CustomItem;
import ru.violence.xholo.api.InteractionData;
import ru.violence.xholo.api.ItemDisplayData;
import ru.violence.xholo.api.TextDisplayData;
import ru.violence.xholo.util.updateflags.UpdateFlag;
import ru.violence.xholo.util.updateflags.UpdateFlags;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class NMSUtil {
    private final AtomicInteger ENTITY_COUNTER = ReflectionUtil.getFieldValue(Entity.class, null, "ENTITY_COUNTER");

    private final EntityDataAccessor<Byte> DP_ENTITY_SHARED_FLAGS = ReflectionUtil.getFieldValue(Entity.class, null, "DATA_SHARED_FLAGS_ID");
    private final EntityDataAccessor<Optional<Component>> DP_ENTITY_CUSTOM_NAME = ReflectionUtil.getFieldValue(Entity.class, null, "DATA_CUSTOM_NAME");
    private final EntityDataAccessor<Boolean> DP_ENTITY_CUSTOM_NAME_VISIBLE = ReflectionUtil.getFieldValue(Entity.class, null, "DATA_CUSTOM_NAME_VISIBLE");

    private final EntityDataAccessor<Byte> DP_ARMOR_STAND_CLIENT_FLAGS = ArmorStand.DATA_CLIENT_FLAGS;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_HEAD_POSE = ArmorStand.DATA_HEAD_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_BODY_POSE = ArmorStand.DATA_BODY_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_LEFT_ARM_POSE = ArmorStand.DATA_LEFT_ARM_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_RIGHT_ARM_POSE = ArmorStand.DATA_RIGHT_ARM_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_LEFT_LEG_POSE = ArmorStand.DATA_LEFT_LEG_POSE;
    private final EntityDataAccessor<Rotations> DP_ARMOR_STAND_RIGHT_LEG_POSE = ArmorStand.DATA_RIGHT_LEG_POSE;

    private final EntityDataAccessor<Integer> DP_DISPLAY_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS = ReflectionUtil.getFieldValue(Display.class, null, "DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID");
    private final EntityDataAccessor<Integer> DP_DISPLAY_TRANSFORMATION_INTERPOLATION_DURATION = ReflectionUtil.getFieldValue(Display.class, null, "DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID");
    private final EntityDataAccessor<Integer> DP_DISPLAY_POS_ROT_INTERPOLATION_DURATION = Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID;
    private final EntityDataAccessor<Vector3f> DP_DISPLAY_TRANSLATION = ReflectionUtil.getFieldValue(Display.class, null, "DATA_TRANSLATION_ID");
    private final EntityDataAccessor<Quaternionf> DP_DISPLAY_LEFT_ROTATION = ReflectionUtil.getFieldValue(Display.class, null, "DATA_LEFT_ROTATION_ID");
    private final EntityDataAccessor<Vector3f> DP_DISPLAY_SCALE = ReflectionUtil.getFieldValue(Display.class, null, "DATA_SCALE_ID");
    private final EntityDataAccessor<Quaternionf> DP_DISPLAY_RIGHT_ROTATION = ReflectionUtil.getFieldValue(Display.class, null, "DATA_RIGHT_ROTATION_ID");
    private final EntityDataAccessor<Byte> DP_DISPLAY_BILLBOARD_RENDER_CONSTRAINTS = ReflectionUtil.getFieldValue(Display.class, null, "DATA_BILLBOARD_RENDER_CONSTRAINTS_ID");
    private final EntityDataAccessor<Integer> DP_DISPLAY_BRIGHTNESS_OVERRIDE = ReflectionUtil.getFieldValue(Display.class, null, "DATA_BRIGHTNESS_OVERRIDE_ID");
    private final EntityDataAccessor<Float> DP_DISPLAY_SHADOW_RADIUS = ReflectionUtil.getFieldValue(Display.class, null, "DATA_SHADOW_RADIUS_ID");
    private final EntityDataAccessor<Float> DP_DISPLAY_SHADOW_STRENGTH = ReflectionUtil.getFieldValue(Display.class, null, "DATA_SHADOW_STRENGTH_ID");
    private final EntityDataAccessor<Float> DP_DISPLAY_WIDTH = ReflectionUtil.getFieldValue(Display.class, null, "DATA_WIDTH_ID");
    private final EntityDataAccessor<Float> DP_DISPLAY_HEIGHT = ReflectionUtil.getFieldValue(Display.class, null, "DATA_HEIGHT_ID");
    private final EntityDataAccessor<Integer> DP_DISPLAY_GLOW_COLOR_OVERRIDE = ReflectionUtil.getFieldValue(Display.class, null, "DATA_GLOW_COLOR_OVERRIDE_ID");

    private final EntityDataAccessor<BlockState> DP_BLOCK_DISPLAY_BLOCK = ReflectionUtil.getFieldValue(Display.BlockDisplay.class, null, "DATA_BLOCK_STATE_ID");

    private final EntityDataAccessor<net.minecraft.world.item.ItemStack> DP_ITEM_DISPLAY_ITEM = ReflectionUtil.getFieldValue(Display.ItemDisplay.class, null, "DATA_ITEM_STACK_ID");
    private final EntityDataAccessor<Byte> DP_ITEM_DISPLAY_TRANSFORM = ReflectionUtil.getFieldValue(Display.ItemDisplay.class, null, "DATA_ITEM_DISPLAY_ID");

    private final EntityDataAccessor<Component> DP_TEXT_DISPLAY_TEXT = ReflectionUtil.getFieldValue(Display.TextDisplay.class, null, "DATA_TEXT_ID");
    private final EntityDataAccessor<Integer> DP_TEXT_DISPLAY_LINE_WIDTH = Display.TextDisplay.DATA_LINE_WIDTH_ID;
    private final EntityDataAccessor<Integer> DP_TEXT_DISPLAY_BACKGROUND_COLOR = Display.TextDisplay.DATA_BACKGROUND_COLOR_ID;
    private final EntityDataAccessor<Byte> DP_TEXT_DISPLAY_OPACITY = ReflectionUtil.getFieldValue(Display.TextDisplay.class, null, "DATA_TEXT_OPACITY_ID");
    private final EntityDataAccessor<Byte> DP_TEXT_DISPLAY_STYLE_FLAGS = ReflectionUtil.getFieldValue(Display.TextDisplay.class, null, "DATA_STYLE_FLAGS_ID");

    private final EntityDataAccessor<Float> DP_INTERACTION_WIDTH = ReflectionUtil.getFieldValue(Interaction.class, null, "DATA_WIDTH_ID");
    private final EntityDataAccessor<Float> DP_INTERACTION_HEIGHT = ReflectionUtil.getFieldValue(Interaction.class, null, "DATA_HEIGHT_ID");
    private final EntityDataAccessor<Boolean> DP_INTERACTION_RESPONSE = ReflectionUtil.getFieldValue(Interaction.class, null, "DATA_RESPONSE_ID");

    private final MethodHandle METHOD_TRACKEDENTITY_GETEFFECTIVERANGE;

    static {
        try {
            METHOD_TRACKEDENTITY_GETEFFECTIVERANGE = MethodHandles.privateLookupIn(ChunkMap.TrackedEntity.class, MethodHandles.lookup()).findVirtual(ChunkMap.TrackedEntity.class, "getEffectiveRange", MethodType.methodType(int.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRealPlayer(@NotNull Player player) {
        if (player instanceof CraftPlayer) {
            ServerPlayer handle = ((CraftPlayer) player).getHandle();
            return handle != null && handle.connection != null;
        }
        return false;
    }

    public boolean isRemoved(@NotNull org.bukkit.entity.Entity entity) {
        if (entity instanceof CraftPlayer) {
            ServerPlayer handle = ((CraftPlayer) entity).getHandle();
            return handle == null || handle.isRemoved();
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
        List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>(3);

        packets.add(createSpawnEntityPacket(location, entityId, EntityType.ARMOR_STAND));
        packets.add(createSetArmorStandMetadataPacket(player, entityId, data, null));

        ClientboundSetEquipmentPacket equipmentPacket = createEquipmentPacket(entityId, new Map.Entry[]{
                new AbstractMap.SimpleEntry<>(EquipmentSlot.HAND, mainHandItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.OFF_HAND, offHandItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.HEAD, headItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.CHEST, chestItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.LEGS, legsItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.FEET, feetItem)
        }, true);
        if (equipmentPacket != null) packets.add(equipmentPacket);

        sendPacket(player, new ClientboundBundlePacket(packets));
    }

    public void spawnEntityArmorStand(@NotNull Player player, int entityId, @NotNull Location location, @NotNull ArmorStandData data, @NotNull org.bukkit.entity.Entity vehicle,
                                      @Nullable ItemStack mainHandItem,
                                      @Nullable ItemStack offHandItem,
                                      @Nullable ItemStack headItem,
                                      @Nullable ItemStack chestItem,
                                      @Nullable ItemStack legsItem,
                                      @Nullable ItemStack feetItem) {
        List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>(3);

        packets.add(createSpawnEntityPacket(location, entityId, EntityType.ARMOR_STAND));
        packets.add(createSetArmorStandMetadataPacket(player, entityId, data, null));

        ClientboundSetEquipmentPacket equipmentPacket = createEquipmentPacket(entityId, new Map.Entry[]{
                new AbstractMap.SimpleEntry<>(EquipmentSlot.HAND, mainHandItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.OFF_HAND, offHandItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.HEAD, headItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.CHEST, chestItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.LEGS, legsItem),
                new AbstractMap.SimpleEntry<>(EquipmentSlot.FEET, feetItem)
        }, true);
        if (equipmentPacket != null) packets.add(equipmentPacket);

        packets.add(createSetPassengersPacket(vehicle.getEntityId(), entityId));

        sendPacket(player, new ClientboundBundlePacket(packets));
    }

    public void spawnEntityBlockDisplay(@NotNull Player player, int entityId, @NotNull Location location, @NotNull BlockDisplayData data) {
        sendPacket(player, new ClientboundBundlePacket(List.of(
                createSpawnEntityPacket(location, entityId, EntityType.BLOCK_DISPLAY),
                createSetBlockDisplayMetadataPacket(entityId, data, null)
        )));
    }

    public void spawnEntityBlockDisplay(@NotNull Player player, int entityId, @NotNull Location location, @NotNull BlockDisplayData data, @NotNull org.bukkit.entity.Entity vehicle) {
        sendPacket(player, new ClientboundBundlePacket(List.of(
                createSpawnEntityPacket(location, entityId, EntityType.BLOCK_DISPLAY),
                createSetBlockDisplayMetadataPacket(entityId, data, null),
                createSetPassengersPacket(vehicle.getEntityId(), entityId)
        )));
    }

    public void spawnEntityItemDisplay(@NotNull Player player, int entityId, @NotNull Location location, @NotNull ItemDisplayData data) {
        sendPacket(player, new ClientboundBundlePacket(List.of(
                createSpawnEntityPacket(location, entityId, EntityType.ITEM_DISPLAY),
                createSetItemDisplayMetadataPacket(player, entityId, data, null)
        )));
    }

    public void spawnEntityItemDisplay(@NotNull Player player, int entityId, @NotNull Location location, @NotNull ItemDisplayData data, @NotNull org.bukkit.entity.Entity vehicle) {
        sendPacket(player, new ClientboundBundlePacket(List.of(
                createSpawnEntityPacket(location, entityId, EntityType.ITEM_DISPLAY),
                createSetItemDisplayMetadataPacket(player, entityId, data, null),
                createSetPassengersPacket(vehicle.getEntityId(), entityId)
        )));
    }

    public void spawnEntityTextDisplay(@NotNull Player player, int entityId, @NotNull Location location, @NotNull TextDisplayData data) {
        sendPacket(player, new ClientboundBundlePacket(List.of(
                createSpawnEntityPacket(location, entityId, EntityType.TEXT_DISPLAY),
                createSetTextDisplayMetadataPacket(player, entityId, data, null)
        )));
    }

    public void spawnEntityTextDisplay(@NotNull Player player, int entityId, @NotNull Location location, @NotNull TextDisplayData data, @NotNull org.bukkit.entity.Entity vehicle) {
        sendPacket(player, new ClientboundBundlePacket(List.of(
                createSpawnEntityPacket(location, entityId, EntityType.TEXT_DISPLAY),
                createSetTextDisplayMetadataPacket(player, entityId, data, null),
                createSetPassengersPacket(vehicle.getEntityId(), entityId)
        )));
    }

    public void spawnEntityInteraction(@NotNull Player player, int entityId, @NotNull Location location, @NotNull InteractionData data) {
        sendPacket(player, new ClientboundBundlePacket(List.of(
                createSpawnEntityPacket(location, entityId, EntityType.INTERACTION),
                createSetInteractionMetadataPacket(player, entityId, data, null)
        )));
    }

    public void spawnEntityInteraction(@NotNull Player player, int entityId, @NotNull Location location, @NotNull InteractionData data, @NotNull org.bukkit.entity.Entity vehicle) {
        sendPacket(player, new ClientboundBundlePacket(List.of(
                createSpawnEntityPacket(location, entityId, EntityType.INTERACTION),
                createSetInteractionMetadataPacket(player, entityId, data, null),
                createSetPassengersPacket(vehicle.getEntityId(), entityId)
        )));
    }

    public void updateArmorStandMetadata(@NotNull Player player, int entityId, @NotNull ArmorStandData data, @Nullable List<UpdateFlag<?>> flags) {
        sendPacket(player, createSetArmorStandMetadataPacket(player, entityId, data, flags));
    }

    public void updateBlockDisplayMetadata(@NotNull Player player, int entityId, @NotNull BlockDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        sendPacket(player, createSetBlockDisplayMetadataPacket(entityId, data, flags));
    }

    public void updateItemDisplayMetadata(@NotNull Player player, int entityId, @NotNull ItemDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        sendPacket(player, createSetItemDisplayMetadataPacket(player, entityId, data, flags));
    }

    public void updateTextDisplayMetadata(@NotNull Player player, int entityId, @NotNull TextDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        sendPacket(player, createSetTextDisplayMetadataPacket(player, entityId, data, flags));
    }

    public void updateInteractionMetadata(@NotNull Player player, int entityId, @NotNull InteractionData data, @Nullable List<UpdateFlag<?>> flags) {
        sendPacket(player, createSetInteractionMetadataPacket(player, entityId, data, flags));
    }

    public void sendEquipment(@NotNull Player player, int entityId,
                              @NotNull Map.Entry<EquipmentSlot, ItemStack> @Nullable [] equipment,
                              boolean isOnSpawn) {
        ClientboundSetEquipmentPacket packet = createEquipmentPacket(entityId, equipment, isOnSpawn);
        if (packet != null) sendPacket(player, packet);
    }

    @Contract(pure = true)
    public @NotNull ClientboundAddEntityPacket createSpawnEntityPacket(@NotNull Location location, int entityId, @NotNull EntityType<?> type) {
        return new ClientboundAddEntityPacket(
                entityId,
                UUID.randomUUID(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw(),
                type,
                0,
                Vec3.ZERO,
                location.getYaw()
        );
    }

    @Contract(pure = true)
    public @Nullable ClientboundSetEquipmentPacket createEquipmentPacket(int entityId,
                                                                         @NotNull Map.Entry<EquipmentSlot, ItemStack> @Nullable [] equipment,
                                                                         boolean isOnSpawn) {
        if (equipment == null || equipment.length == 0) return null;

        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>(equipment.length);

        for (Map.Entry<EquipmentSlot, ItemStack> pair : equipment) {
            if (isOnSpawn && (pair.getValue() == null || pair.getValue().getType() == Material.AIR)) continue;
            equipmentList.add(Pair.of(toNMS(pair.getKey()), CraftItemStack.asNMSCopy(pair.getValue())));
        }

        if (equipmentList.isEmpty()) return null;

        return new ClientboundSetEquipmentPacket(entityId, equipmentList);
    }

    @Contract(pure = true)
    public @NotNull ClientboundSetEntityDataPacket createSetArmorStandMetadataPacket(@NotNull Player player, int entityId, @NotNull ArmorStandData data, @Nullable List<UpdateFlag<?>> flags) {
        List<SynchedEntityData.DataValue<?>> dataValues = flags == null || flags.isEmpty()
                ? createArmorStandDataWatcher(player, data)
                : createArmorStandDataWatcher(player, data, flags);

        return new ClientboundSetEntityDataPacket(entityId, dataValues);
    }

    @Contract(pure = true)
    public @NotNull ClientboundSetEntityDataPacket createSetBlockDisplayMetadataPacket(int entityId, @NotNull BlockDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        List<SynchedEntityData.DataValue<?>> dataValues = flags == null || flags.isEmpty()
                ? createBlockDisplayDataWatcher(data)
                : createBlockDisplayDataWatcher(data, flags);

        return new ClientboundSetEntityDataPacket(entityId, dataValues);
    }

    @Contract(pure = true)
    public @NotNull ClientboundSetEntityDataPacket createSetItemDisplayMetadataPacket(@NotNull Player player, int entityId, @NotNull ItemDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        List<SynchedEntityData.DataValue<?>> dataValues = flags == null || flags.isEmpty()
                ? createItemDisplayDataWatcher(player, data)
                : createItemDisplayDataWatcher(player, data, flags);

        return new ClientboundSetEntityDataPacket(entityId, dataValues);
    }

    @Contract(pure = true)
    public @NotNull ClientboundSetEntityDataPacket createSetTextDisplayMetadataPacket(@NotNull Player player, int entityId, @NotNull TextDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        List<SynchedEntityData.DataValue<?>> dataValues = flags == null || flags.isEmpty()
                ? createTextDisplayDataWatcher(player, data)
                : createTextDisplayDataWatcher(player, data, flags);

        return new ClientboundSetEntityDataPacket(entityId, dataValues);
    }

    @Contract(pure = true)
    public @NotNull ClientboundSetEntityDataPacket createSetInteractionMetadataPacket(@NotNull Player player, int entityId, @NotNull InteractionData data, @Nullable List<UpdateFlag<?>> flags) {
        List<SynchedEntityData.DataValue<?>> dataValues = flags == null || flags.isEmpty()
                ? createInteractionDataWatcher(player, data)
                : createInteractionDataWatcher(player, data, flags);

        return new ClientboundSetEntityDataPacket(entityId, dataValues);
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

        sendPacket(player, ClientboundTeleportEntityPacket.STREAM_CODEC.decode(fbb));
    }

    public void destroyEntities(@NotNull Player player, int @NotNull ... entityIds) {
        sendPacket(player, new ClientboundRemoveEntitiesPacket(entityIds));
    }

    public @NotNull ClientboundSetPassengersPacket createSetPassengersPacket(int vehicleId, int @NotNull ... passengerIds) {
        FriendlyByteBuf fbb = new FriendlyByteBuf(Unpooled.buffer());

        fbb.writeVarInt(vehicleId);
        fbb.writeVarIntArray(passengerIds);

        return ClientboundSetPassengersPacket.STREAM_CODEC.decode(fbb);
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
    private @NotNull List<SynchedEntityData.DataValue<?>> createArmorStandDataWatcher(@NotNull Player player, @NotNull ArmorStandData data) {
        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        // Set entity flags
        setDWEntityFlags(dataValues, !data.isVisible(), data.isGlowing());

        // Set custom name
        setDWCustomName(dataValues, data.getCustomName() != null ? data.getCustomName().apply(player) : null);

        // Set ArmorStand status
        setDWArmorStandClientFlags(dataValues, data.isSmall(), data.isHasArms(), !data.isHasBasePlate(), data.isMarker());

        // Set rotations
        if (data.getHeadPose() != null) setDWArmorStandHeadPose(dataValues, data.getHeadPose());
        if (data.getBodyPose() != null) setDWArmorStandBodyPose(dataValues, data.getBodyPose());
        if (data.getLeftArmPose() != null) setDWArmorStandLeftArmPose(dataValues, data.getLeftArmPose());
        if (data.getRightArmPose() != null) setDWArmorStandRightArmPose(dataValues, data.getRightArmPose());
        if (data.getLeftLegPose() != null) setDWArmorStandLeftLegPose(dataValues, data.getLeftLegPose());
        if (data.getRightLegPose() != null) setDWArmorStandRightLegPose(dataValues, data.getRightLegPose());

        return dataValues;
    }

    @Contract(pure = true)
    public @NotNull List<SynchedEntityData.DataValue<?>> createArmorStandDataWatcher(@NotNull Player player, @NotNull ArmorStandData data, @Nullable List<UpdateFlag<?>> flags) {
        if (flags == null || flags.isEmpty()) return createArmorStandDataWatcher(player, data);

        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        for (UpdateFlag<?> flag : flags) {
            if (flag.equals(UpdateFlags.ARMOR_STAND_STATUS)) {
                setDWArmorStandClientFlags(dataValues, data.isSmall(), data.isHasArms(), !data.isHasBasePlate(), data.isMarker());
            } else if (flag.equals(UpdateFlags.ARMOR_STAND_FLAGS)) {
                setDWEntityFlags(dataValues, !data.isVisible(), data.isGlowing());
            } else if (flag.equals(UpdateFlags.ARMOR_STAND_CUSTOM_NAME)) {
                setDWCustomName(dataValues, data.getCustomName() != null ? data.getCustomName().apply(player) : null);
            } else if (flag.equals(UpdateFlags.ARMOR_STAND_BODY_POSE)) {
                if (data.getBodyPose() != null) setDWArmorStandBodyPose(dataValues, data.getBodyPose());
            } else if (flag.equals(UpdateFlags.ARMOR_STAND_LEFT_ARM_POSE)) {
                if (data.getLeftArmPose() != null) setDWArmorStandLeftArmPose(dataValues, data.getLeftArmPose());
            } else if (flag.equals(UpdateFlags.ARMOR_STAND_RIGHT_ARM_POSE)) {
                if (data.getRightArmPose() != null) setDWArmorStandRightArmPose(dataValues, data.getRightArmPose());
            } else if (flag.equals(UpdateFlags.ARMOR_STAND_LEFT_LEG_POSE)) {
                if (data.getLeftLegPose() != null) setDWArmorStandLeftLegPose(dataValues, data.getLeftLegPose());
            } else if (flag.equals(UpdateFlags.ARMOR_STAND_RIGHT_LEG_POSE)) {
                if (data.getRightLegPose() != null) setDWArmorStandRightLegPose(dataValues, data.getRightLegPose());
            } else if (flag.equals(UpdateFlags.ARMOR_STAND_HEAD_POSE)) {
                if (data.getHeadPose() != null) setDWArmorStandHeadPose(dataValues, data.getHeadPose());
            } else {
                throw new IllegalStateException("Unexpected flag: " + flag);
            }
        }

        return dataValues;
    }

    @Contract(pure = true)
    private @NotNull List<SynchedEntityData.DataValue<?>> createBlockDisplayDataWatcher(@NotNull BlockDisplayData data) {
        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        // Set entity flags
        setDWEntityFlags(dataValues, !data.isVisible(), data.isGlowing());

        { // Set display data
            setDWDisplayTransformation(dataValues, data.getTransformation());
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_DURATION, data.getInterpolationDuration()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_POS_ROT_INTERPOLATION_DURATION, data.getTeleportDuration()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_RADIUS, data.getShadowRadius()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_STRENGTH, data.getShadowStrength()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_WIDTH, data.getDisplayWidth()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_HEIGHT, data.getDisplayHeight()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS, data.getInterpolationDelay()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BILLBOARD_RENDER_CONSTRAINTS, toNMS(data.getBillboard())));

            {
                int color = data.getGlowColorOverride() != null ? data.getGlowColorOverride().asARGB() : 0;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_GLOW_COLOR_OVERRIDE, color));
            }
            {
                org.bukkit.entity.Display.Brightness brightness = data.getBrightness();
                int nmsBrightness = data.getBrightness() != null ? new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()).pack() : -1;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BRIGHTNESS_OVERRIDE, nmsBrightness));
            }
        } // Set display data

        { // Set block display data
            BlockState nmsBlock = ((CraftBlockData) data.getBlock()).getState();
            dataValues.add(SynchedEntityData.DataValue.create(DP_BLOCK_DISPLAY_BLOCK, nmsBlock));
        } // Set block display data

        return dataValues;
    }

    @Contract(pure = true)
    public @NotNull List<SynchedEntityData.DataValue<?>> createBlockDisplayDataWatcher(@NotNull BlockDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        if (flags == null || flags.isEmpty()) return createBlockDisplayDataWatcher(data);

        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        for (UpdateFlag<?> flag : flags) {
            if (flag.equals(UpdateFlags.DISPLAY_TRANSFORMATION)) {
                setDWDisplayTransformation(dataValues, data.getTransformation());
            } else if (flag.equals(UpdateFlags.DISPLAY_INTERPOLATION_DURATION)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_DURATION, data.getInterpolationDuration()));
            } else if (flag.equals(UpdateFlags.DISPLAY_TELEPORT_DURATION)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_POS_ROT_INTERPOLATION_DURATION, data.getTeleportDuration()));
            } else if (flag.equals(UpdateFlags.DISPLAY_SHADOW_RADIUS)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_RADIUS, data.getShadowRadius()));
            } else if (flag.equals(UpdateFlags.DISPLAY_SHADOW_STRENGTH)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_STRENGTH, data.getShadowStrength()));
            } else if (flag.equals(UpdateFlags.DISPLAY_WIDTH)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_WIDTH, data.getDisplayWidth()));
            } else if (flag.equals(UpdateFlags.DISPLAY_HEIGHT)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_HEIGHT, data.getDisplayHeight()));
            } else if (flag.equals(UpdateFlags.DISPLAY_INTERPOLATION_DELAY)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS, data.getInterpolationDelay()));
            } else if (flag.equals(UpdateFlags.DISPLAY_BILLBOARD)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BILLBOARD_RENDER_CONSTRAINTS, toNMS(data.getBillboard())));
            } else if (flag.equals(UpdateFlags.DISPLAY_GLOW_COLOR_OVERRIDE)) {
                int color = data.getGlowColorOverride() != null ? data.getGlowColorOverride().asARGB() : 0;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_GLOW_COLOR_OVERRIDE, color));
            } else if (flag.equals(UpdateFlags.DISPLAY_BRIGHTNESS)) {
                org.bukkit.entity.Display.Brightness brightness = data.getBrightness();
                int nmsBrightness = data.getBrightness() != null ? new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()).pack() : -1;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BRIGHTNESS_OVERRIDE, nmsBrightness));
            } else if (flag.equals(UpdateFlags.BLOCK_DISPLAY_BLOCK)) {
                BlockState nmsBlock = ((CraftBlockData) data.getBlock()).getState();
                dataValues.add(SynchedEntityData.DataValue.create(DP_BLOCK_DISPLAY_BLOCK, nmsBlock));
            } else {
                throw new IllegalStateException("Unexpected flag: " + flag);
            }
        }

        return dataValues;
    }

    @Contract(pure = true)
    private @NotNull List<SynchedEntityData.DataValue<?>> createItemDisplayDataWatcher(@NotNull Player player, @NotNull ItemDisplayData data) {
        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        // Set entity flags
        setDWEntityFlags(dataValues, !data.isVisible(), data.isGlowing());

        { // Set display data
            setDWDisplayTransformation(dataValues, data.getTransformation());
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_DURATION, data.getInterpolationDuration()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_POS_ROT_INTERPOLATION_DURATION, data.getTeleportDuration()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_RADIUS, data.getShadowRadius()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_STRENGTH, data.getShadowStrength()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_WIDTH, data.getDisplayWidth()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_HEIGHT, data.getDisplayHeight()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS, data.getInterpolationDelay()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BILLBOARD_RENDER_CONSTRAINTS, toNMS(data.getBillboard())));

            {
                int color = data.getGlowColorOverride() != null ? data.getGlowColorOverride().asARGB() : 0;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_GLOW_COLOR_OVERRIDE, color));
            }
            {
                org.bukkit.entity.Display.Brightness brightness = data.getBrightness();
                int nmsBrightness = data.getBrightness() != null ? new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()).pack() : -1;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BRIGHTNESS_OVERRIDE, nmsBrightness));
            }
        } // Set display data

        { // Set item display data
            CustomItem customItem = data.getItem();
            dataValues.add(SynchedEntityData.DataValue.create(DP_ITEM_DISPLAY_ITEM, CraftItemStack.asNMSCopy(customItem != null ? customItem.apply(player) : null)));
            dataValues.add(SynchedEntityData.DataValue.create(DP_ITEM_DISPLAY_TRANSFORM, ItemDisplayContext.BY_ID.apply(data.getDisplayTransform().ordinal()).getId()));
        } // Set item display data

        return dataValues;
    }

    @Contract(pure = true)
    public @NotNull List<SynchedEntityData.DataValue<?>> createItemDisplayDataWatcher(@NotNull Player player, @NotNull ItemDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        if (flags == null || flags.isEmpty()) return createItemDisplayDataWatcher(player, data);

        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        for (UpdateFlag<?> flag : flags) {
            if (flag.equals(UpdateFlags.DISPLAY_TRANSFORMATION)) {
                setDWDisplayTransformation(dataValues, data.getTransformation());
            } else if (flag.equals(UpdateFlags.DISPLAY_INTERPOLATION_DURATION)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_DURATION, data.getInterpolationDuration()));
            } else if (flag.equals(UpdateFlags.DISPLAY_TELEPORT_DURATION)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_POS_ROT_INTERPOLATION_DURATION, data.getTeleportDuration()));
            } else if (flag.equals(UpdateFlags.DISPLAY_SHADOW_RADIUS)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_RADIUS, data.getShadowRadius()));
            } else if (flag.equals(UpdateFlags.DISPLAY_SHADOW_STRENGTH)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_STRENGTH, data.getShadowStrength()));
            } else if (flag.equals(UpdateFlags.DISPLAY_WIDTH)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_WIDTH, data.getDisplayWidth()));
            } else if (flag.equals(UpdateFlags.DISPLAY_HEIGHT)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_HEIGHT, data.getDisplayHeight()));
            } else if (flag.equals(UpdateFlags.DISPLAY_INTERPOLATION_DELAY)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS, data.getInterpolationDelay()));
            } else if (flag.equals(UpdateFlags.DISPLAY_BILLBOARD)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BILLBOARD_RENDER_CONSTRAINTS, toNMS(data.getBillboard())));
            } else if (flag.equals(UpdateFlags.DISPLAY_GLOW_COLOR_OVERRIDE)) {
                int color = data.getGlowColorOverride() != null ? data.getGlowColorOverride().asARGB() : 0;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_GLOW_COLOR_OVERRIDE, color));
            } else if (flag.equals(UpdateFlags.DISPLAY_BRIGHTNESS)) {
                org.bukkit.entity.Display.Brightness brightness = data.getBrightness();
                int nmsBrightness = data.getBrightness() != null ? new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()).pack() : -1;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BRIGHTNESS_OVERRIDE, nmsBrightness));
            } else if (flag.equals(UpdateFlags.ITEM_DISPLAY_ITEM)) {
                CustomItem customItem = data.getItem();
                dataValues.add(SynchedEntityData.DataValue.create(DP_ITEM_DISPLAY_ITEM, CraftItemStack.asNMSCopy(customItem != null ? customItem.apply(player) : null)));
            } else if (flag.equals(UpdateFlags.ITEM_DISPLAY_TRANSFORM)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_ITEM_DISPLAY_TRANSFORM, ItemDisplayContext.BY_ID.apply(data.getDisplayTransform().ordinal()).getId()));
            } else {
                throw new IllegalStateException("Unexpected flag: " + flag);
            }
        }

        return dataValues;
    }

    @Contract(pure = true)
    private @NotNull List<SynchedEntityData.DataValue<?>> createTextDisplayDataWatcher(@NotNull Player player, @NotNull TextDisplayData data) {
        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        // Set entity flags
        setDWEntityFlags(dataValues, !data.isVisible(), data.isGlowing());

        { // Set display data
            setDWDisplayTransformation(dataValues, data.getTransformation());
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_DURATION, data.getInterpolationDuration()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_POS_ROT_INTERPOLATION_DURATION, data.getTeleportDuration()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_RADIUS, data.getShadowRadius()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_STRENGTH, data.getShadowStrength()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_WIDTH, data.getDisplayWidth()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_HEIGHT, data.getDisplayHeight()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS, data.getInterpolationDelay()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BILLBOARD_RENDER_CONSTRAINTS, toNMS(data.getBillboard())));

            {
                int color = data.getGlowColorOverride() != null ? data.getGlowColorOverride().asARGB() : 0;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_GLOW_COLOR_OVERRIDE, color));
            }
            {
                org.bukkit.entity.Display.Brightness brightness = data.getBrightness();
                int nmsBrightness = data.getBrightness() != null ? new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()).pack() : -1;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BRIGHTNESS_OVERRIDE, nmsBrightness));
            }
        } // Set display data

        { // Set text display data
            {
                Component nmsComponent = PaperAdventure.asVanilla(data.getText().apply(player));
                dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_TEXT, nmsComponent));
            }

            dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_LINE_WIDTH, data.getLineWidth()));

            {
                int color = data.getBackgroundColor() != null ? data.getBackgroundColor().asARGB() : -1;
                dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_BACKGROUND_COLOR, color));
            }

            dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_OPACITY, data.getTextOpacity()));

            {
                boolean alignLeft;
                boolean alignRight;
                switch (data.getAlignment()) {
                    case LEFT -> {
                        alignLeft = true;
                        alignRight = false;
                    }
                    case RIGHT -> {
                        alignLeft = false;
                        alignRight = true;
                    }
                    default -> {
                        alignLeft = false;
                        alignRight = false;
                    }
                }
                setDWTextDisplayStyleFlags(dataValues, data.isShadowed(), data.isSeeThrough(), data.isDefaultBackground(), alignLeft, alignRight);
            }
        } // Set text display data

        return dataValues;
    }

    @Contract(pure = true)
    public @NotNull List<SynchedEntityData.DataValue<?>> createTextDisplayDataWatcher(@NotNull Player player, @NotNull TextDisplayData data, @Nullable List<UpdateFlag<?>> flags) {
        if (flags == null || flags.isEmpty()) return createTextDisplayDataWatcher(player, data);

        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        for (UpdateFlag<?> flag : flags) {
            if (flag.equals(UpdateFlags.DISPLAY_TRANSFORMATION)) {
                setDWDisplayTransformation(dataValues, data.getTransformation());
            } else if (flag.equals(UpdateFlags.DISPLAY_INTERPOLATION_DURATION)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_DURATION, data.getInterpolationDuration()));
            } else if (flag.equals(UpdateFlags.DISPLAY_TELEPORT_DURATION)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_POS_ROT_INTERPOLATION_DURATION, data.getTeleportDuration()));
            } else if (flag.equals(UpdateFlags.DISPLAY_SHADOW_RADIUS)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_RADIUS, data.getShadowRadius()));
            } else if (flag.equals(UpdateFlags.DISPLAY_SHADOW_STRENGTH)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SHADOW_STRENGTH, data.getShadowStrength()));
            } else if (flag.equals(UpdateFlags.DISPLAY_WIDTH)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_WIDTH, data.getDisplayWidth()));
            } else if (flag.equals(UpdateFlags.DISPLAY_HEIGHT)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_HEIGHT, data.getDisplayHeight()));
            } else if (flag.equals(UpdateFlags.DISPLAY_INTERPOLATION_DELAY)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS, data.getInterpolationDelay()));
            } else if (flag.equals(UpdateFlags.DISPLAY_BILLBOARD)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BILLBOARD_RENDER_CONSTRAINTS, toNMS(data.getBillboard())));
            } else if (flag.equals(UpdateFlags.DISPLAY_GLOW_COLOR_OVERRIDE)) {
                int color = data.getGlowColorOverride() != null ? data.getGlowColorOverride().asARGB() : 0;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_GLOW_COLOR_OVERRIDE, color));
            } else if (flag.equals(UpdateFlags.DISPLAY_BRIGHTNESS)) {
                org.bukkit.entity.Display.Brightness brightness = data.getBrightness();
                int nmsBrightness = data.getBrightness() != null ? new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()).pack() : -1;
                dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_BRIGHTNESS_OVERRIDE, nmsBrightness));
            } else if (flag.equals(UpdateFlags.TEXT_DISPLAY_TEXT)) {
                Component nmsComponent = PaperAdventure.asVanilla(data.getText().apply(player));
                dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_TEXT, nmsComponent));
            } else if (flag.equals(UpdateFlags.TEXT_DISPLAY_LINE_WIDTH)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_LINE_WIDTH, data.getLineWidth()));
            } else if (flag.equals(UpdateFlags.TEXT_DISPLAY_BACKGROUND_COLOR)) {
                int color = data.getBackgroundColor() != null ? data.getBackgroundColor().asARGB() : -1;
                dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_BACKGROUND_COLOR, color));
            } else if (flag.equals(UpdateFlags.TEXT_DISPLAY_OPACITY)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_OPACITY, data.getTextOpacity()));
            } else if (flag.equals(UpdateFlags.TEXT_DISPLAY_STYLE)) {
                boolean alignLeft;
                boolean alignRight;
                switch (data.getAlignment()) {
                    case LEFT -> {
                        alignLeft = true;
                        alignRight = false;
                    }
                    case RIGHT -> {
                        alignLeft = false;
                        alignRight = true;
                    }
                    default -> {
                        alignLeft = false;
                        alignRight = false;
                    }
                }
                setDWTextDisplayStyleFlags(dataValues, data.isShadowed(), data.isSeeThrough(), data.isDefaultBackground(), alignLeft, alignRight);
            } else {
                throw new IllegalStateException("Unexpected flag: " + flag);
            }
        }

        return dataValues;
    }

    @Contract(pure = true)
    private @NotNull List<SynchedEntityData.DataValue<?>> createInteractionDataWatcher(@NotNull Player player, @NotNull InteractionData data) {
        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        // Set entity flags
        setDWEntityFlags(dataValues, !data.isVisible(), data.isGlowing());

        { // Set interaction data
            dataValues.add(SynchedEntityData.DataValue.create(DP_INTERACTION_WIDTH, data.getWidth()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_INTERACTION_HEIGHT, data.getHeight()));
            dataValues.add(SynchedEntityData.DataValue.create(DP_INTERACTION_RESPONSE, data.isResponsive()));
        } // Set interaction data

        return dataValues;
    }

    @Contract(pure = true)
    public @NotNull List<SynchedEntityData.DataValue<?>> createInteractionDataWatcher(@NotNull Player player, @NotNull InteractionData data, @Nullable List<UpdateFlag<?>> flags) {
        if (flags == null || flags.isEmpty()) return createInteractionDataWatcher(player, data);

        List<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();

        for (UpdateFlag<?> flag : flags) {
            if (flag.equals(UpdateFlags.INTERACTION_WIDTH)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_INTERACTION_WIDTH, data.getWidth()));
            } else if (flag.equals(UpdateFlags.INTERACTION_HEIGHT)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_INTERACTION_HEIGHT, data.getHeight()));
            } else if (flag.equals(UpdateFlags.INTERACTION_RESPONSE)) {
                dataValues.add(SynchedEntityData.DataValue.create(DP_INTERACTION_RESPONSE, data.isResponsive()));
            } else {
                throw new IllegalStateException("Unexpected flag: " + flag);
            }
        }

        return dataValues;
    }

    private void setDWTextDisplayStyleFlags(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, boolean shadowed, boolean seeThrough, boolean defaultBackground, boolean alignLeft, boolean alignRight) {
        byte flags = 0;
        flags = setStatus(flags, 0x1, shadowed);
        flags = setStatus(flags, 0x2, seeThrough);
        flags = setStatus(flags, 0x4, defaultBackground);
        flags = setStatus(flags, 0x8, alignLeft);
        flags = setStatus(flags, 0x10, alignRight);
        dataValues.add(SynchedEntityData.DataValue.create(DP_TEXT_DISPLAY_STYLE_FLAGS, flags));
    }

    private void setDWDisplayTransformation(List<SynchedEntityData.DataValue<?>> dataValues, Transformation transformation) {
        com.mojang.math.Transformation nmsTransformation = new com.mojang.math.Transformation(transformation.getTranslation(), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation());
        dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_TRANSLATION, nmsTransformation.getTranslation()));
        dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_LEFT_ROTATION, nmsTransformation.getLeftRotation()));
        dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_SCALE, nmsTransformation.getScale()));
        dataValues.add(SynchedEntityData.DataValue.create(DP_DISPLAY_RIGHT_ROTATION, nmsTransformation.getRightRotation()));
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
        return switch (old) {
            case HAND -> net.minecraft.world.entity.EquipmentSlot.MAINHAND;
            case OFF_HAND -> net.minecraft.world.entity.EquipmentSlot.OFFHAND;
            case FEET -> net.minecraft.world.entity.EquipmentSlot.FEET;
            case LEGS -> net.minecraft.world.entity.EquipmentSlot.LEGS;
            case CHEST -> net.minecraft.world.entity.EquipmentSlot.CHEST;
            case HEAD -> net.minecraft.world.entity.EquipmentSlot.HEAD;
            case BODY -> net.minecraft.world.entity.EquipmentSlot.BODY;
        };
    }

    @Contract(pure = true)
    private byte toNMS(@NotNull org.bukkit.entity.Display.Billboard old) {
        return switch (old) {
            case FIXED -> 0;
            case VERTICAL -> 1;
            case HORIZONTAL -> 2;
            case CENTER -> 3;
        };
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
        ChunkMap.TrackedEntity tracker = ((CraftPlayer) player).getHandle().moonrise$getTrackedEntity();
        if (tracker == null) return 0;
        try {
            return (int) METHOD_TRACKEDENTITY_GETEFFECTIVERANGE.invoke(tracker);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private void setDWEntityFlags(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, boolean invisible, boolean glowing) {
        byte flags = 0;
        flags = setFlag(flags, 0x5, invisible);
        flags = setFlag(flags, 0x6, glowing);
        dataValues.add(SynchedEntityData.DataValue.create(DP_ENTITY_SHARED_FLAGS, flags));
    }

    private void setDWCustomName(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, @Nullable net.kyori.adventure.text.Component customName) {
        Component nmsName = customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null;
        dataValues.add(SynchedEntityData.DataValue.create(DP_ENTITY_CUSTOM_NAME, Optional.ofNullable(nmsName)));
        dataValues.add(SynchedEntityData.DataValue.create(DP_ENTITY_CUSTOM_NAME_VISIBLE, nmsName != null));
    }

    private void setDWArmorStandClientFlags(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, boolean small, boolean hasArms, boolean noBasePlate, boolean marker) {
        byte status = 0;
        status = setStatus(status, 0x1, small);
        status = setStatus(status, 0x4, hasArms);
        status = setStatus(status, 0x8, noBasePlate);
        status = setStatus(status, 0x10, marker);
        dataValues.add(SynchedEntityData.DataValue.create(DP_ARMOR_STAND_CLIENT_FLAGS, status));
    }

    private void setDWArmorStandHeadPose(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, @NotNull EulerAngle pose) {
        dataValues.add(SynchedEntityData.DataValue.create(DP_ARMOR_STAND_HEAD_POSE, toNMS(pose)));
    }

    private void setDWArmorStandBodyPose(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, @NotNull EulerAngle pose) {
        dataValues.add(SynchedEntityData.DataValue.create(DP_ARMOR_STAND_BODY_POSE, toNMS(pose)));
    }

    private void setDWArmorStandLeftArmPose(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, @NotNull EulerAngle pose) {
        dataValues.add(SynchedEntityData.DataValue.create(DP_ARMOR_STAND_LEFT_ARM_POSE, toNMS(pose)));
    }

    private void setDWArmorStandRightArmPose(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, @NotNull EulerAngle pose) {
        dataValues.add(SynchedEntityData.DataValue.create(DP_ARMOR_STAND_RIGHT_ARM_POSE, toNMS(pose)));
    }

    private void setDWArmorStandLeftLegPose(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, @NotNull EulerAngle pose) {
        dataValues.add(SynchedEntityData.DataValue.create(DP_ARMOR_STAND_LEFT_LEG_POSE, toNMS(pose)));
    }

    private void setDWArmorStandRightLegPose(@NotNull List<SynchedEntityData.DataValue<?>> dataValues, @NotNull EulerAngle pose) {
        dataValues.add(SynchedEntityData.DataValue.create(DP_ARMOR_STAND_RIGHT_LEG_POSE, toNMS(pose)));
    }
}
