package ru.violence.xholo.util.nms;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.Vector3f;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.reflection.ReflectField;
import ru.violence.coreapi.common.api.reflection.ReflectMethod;
import ru.violence.coreapi.common.api.reflection.ReflectionUtil;
import ru.violence.coreapi.common.api.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.util.UpdateFlag;

import java.util.UUID;

@UtilityClass
public class NMSUtil {
    private final Object DP_ENTITY_FLAGS = ReflectionUtil.getFieldValue(Entity.class, null, "Z");
    private final Object DP_ENTITY_CUSTOM_NAME = ReflectionUtil.getFieldValue(Entity.class, null, "aB");
    private final Object DP_ENTITY_HAS_CUSTOM_NAME = ReflectionUtil.getFieldValue(Entity.class, null, "aC");

    private final Object DP_ARMOR_STAND_STATUS = ReflectionUtil.getFieldValue(EntityArmorStand.class, null, "a");
    private final Object DP_ARMOR_STAND_HEAD_ROTATION = ReflectionUtil.getFieldValue(EntityArmorStand.class, null, "b");
    private final Object DP_ARMOR_STAND_BODY_ROTATION = ReflectionUtil.getFieldValue(EntityArmorStand.class, null, "c");
    private final Object DP_ARMOR_STAND_LEFT_ARM_ROTATION = ReflectionUtil.getFieldValue(EntityArmorStand.class, null, "d");
    private final Object DP_ARMOR_STAND_RIGHT_ARM_ROTATION = ReflectionUtil.getFieldValue(EntityArmorStand.class, null, "e");
    private final Object DP_ARMOR_STAND_LEFT_LEG_ROTATION = ReflectionUtil.getFieldValue(EntityArmorStand.class, null, "f");
    private final Object DP_ARMOR_STAND_RIGHT_LEG_ROTATION = ReflectionUtil.getFieldValue(EntityArmorStand.class, null, "g");

    private final ReflectMethod<Void> METHOD_DM_REGISTER = new ReflectMethod<>(DataWatcher.class, "register", DataWatcherObject.class, Object.class);

    public boolean isRealPlayer(@NotNull Player player) {
        if (player instanceof CraftPlayer) {
            EntityPlayer handle = ((CraftPlayer) player).getHandle();
            return handle != null && handle.playerConnection != null;
        }
        return false;
    }

    @Contract(pure = true)
    public int getArmorStandEntityTypeId() {
        return 30; // ArmorStand (1.12.2)
    }

    public void spawnEntityArmorStand(@NotNull Player player, int entityId, @NotNull Location location, @NotNull ArmorStandData data) {
        DataWatcher dataWatcher = createDataWatcher(player, data);

        spawnEntityLiving(player, location, getArmorStandEntityTypeId(), entityId, dataWatcher);
    }

    public void spawnEntityLiving(@NotNull Player player, @NotNull Location location, int entityTypeId, int entityId, @NotNull Object dataWatcher) {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "a", entityId);                                               // entityId
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "b", UUID.randomUUID());                                      // uniqueId
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "c", entityTypeId);                                           // typeId
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "d", location.getX());                                        // locX
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "e", location.getY());                                        // locY
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "f", location.getZ());                                        // locZ
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "j", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));   // yaw
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "k", (byte) ((int) (location.getPitch() * 256.0F / 360.0F))); // pitch
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "l", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));   // rotationHeadYaw
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "g", 0);                                                      // velocityX
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "h", 0);                                                      // velocityY
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "i", 0);                                                      // velocityZ
        ReflectionUtil.setFieldValue(PacketPlayOutSpawnEntityLiving.class, packet, "m", dataWatcher);                                            // dataWatcher

        sendPacket(player, packet);
    }

    public void sendEquipment(@NotNull Player player, int entityId, @NotNull EquipmentSlot slot, @Nullable ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        sendPacket(player, new PacketPlayOutEntityEquipment(entityId, toNMS(slot), nmsItem));
    }

    public void updateMetadata(@NotNull Player player, int entityId, @NotNull ArmorStandData data, UpdateFlag @Nullable [] flags) {
        DataWatcher watcher = flags == null || flags.length == 0
                ? createDataWatcher(player, data)
                : createDataWatcher(player, data, flags);
        sendPacket(player, new PacketPlayOutEntityMetadata(entityId, watcher, true));
    }

    public void teleportEntity(@NotNull Player player, int entityId, @NotNull Location location) {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
        ReflectionUtil.setFieldValue(PacketPlayOutEntityTeleport.class, packet, "a", entityId);                                               // entityId
        ReflectionUtil.setFieldValue(PacketPlayOutEntityTeleport.class, packet, "b", location.getX());                                        // posX
        ReflectionUtil.setFieldValue(PacketPlayOutEntityTeleport.class, packet, "c", location.getY());                                        // posY
        ReflectionUtil.setFieldValue(PacketPlayOutEntityTeleport.class, packet, "d", location.getZ());                                        // posZ
        ReflectionUtil.setFieldValue(PacketPlayOutEntityTeleport.class, packet, "e", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));   // yaw
        ReflectionUtil.setFieldValue(PacketPlayOutEntityTeleport.class, packet, "f", (byte) ((int) (location.getPitch() * 256.0F / 360.0F))); // pitch
        ReflectionUtil.setFieldValue(PacketPlayOutEntityTeleport.class, packet, "g", true);                                                   // onGround

        sendPacket(player, packet);
    }

    public void destroyEntities(@NotNull Player player, int @NotNull ... entityIds) {
        sendPacket(player, new PacketPlayOutEntityDestroy(entityIds));
    }

    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        Check.isTrue(Packet.class.isAssignableFrom(packet.getClass()), "Object is not a packet");
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }

    @Contract(pure = true)
    public int getNextEntityId() {
        ReflectField<Integer> field = new ReflectField<>(Entity.class, "entityCount");
        int id = field.getValue(null);
        field.setValue(null, id + 1);
        return id;
    }

    @Contract(pure = true)
    private @NotNull DataWatcher createDataWatcher(@NotNull Player player, @NotNull ArmorStandData data) {
        DataWatcher watcher = new DataWatcher(null);

        // Set entity flags
        setDWEntityFlags(watcher, !data.isVisible());

        // Set custom name
        setDWCustomName(watcher, data.getCustomName() != null ? data.getCustomName().apply(player) : null);

        // Set ArmorStand status
        setDWArmorStandStatus(watcher, data.isSmall(), data.isHasArms(), !data.isHasBasePlate(), data.isMarker());

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
    public @NotNull DataWatcher createDataWatcher(@NotNull Player player, @NotNull ArmorStandData data, UpdateFlag @Nullable [] flags) {
        if (flags == null || flags.length == 0) return createDataWatcher(player, data);

        DataWatcher watcher = new DataWatcher(null);

        for (UpdateFlag flag : flags) {
            switch (flag) {
                case STATUS:
                    setDWArmorStandStatus(watcher, data.isSmall(), data.isHasArms(), !data.isHasBasePlate(), data.isMarker());
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
    private @NotNull Vector3f toNMS(@NotNull EulerAngle old) {
        return new Vector3f(
                (float) Math.toDegrees(old.getX()),
                (float) Math.toDegrees(old.getY()),
                (float) Math.toDegrees(old.getZ())
        );
    }

    @Contract(pure = true)
    private @NotNull EnumItemSlot toNMS(@NotNull EquipmentSlot old) {
        switch (old) {
            case HAND:
                return EnumItemSlot.MAINHAND;
            case OFF_HAND:
                return EnumItemSlot.OFFHAND;
            case FEET:
                return EnumItemSlot.FEET;
            case LEGS:
                return EnumItemSlot.LEGS;
            case CHEST:
                return EnumItemSlot.CHEST;
            case HEAD:
                return EnumItemSlot.HEAD;
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
        return ((CraftPlayer) player).getHandle().getFurthestViewableBlock();
    }

    private void setDWEntityFlags(@NotNull DataWatcher watcher, boolean invisible) {
        byte flags = 0;
        flags = setFlag(flags, 0x5, invisible);
        METHOD_DM_REGISTER.invoke(watcher, DP_ENTITY_FLAGS, flags);
    }

    private void setDWCustomName(@NotNull DataWatcher watcher, @Nullable String customName) {
        if (customName == null) customName = "";
        METHOD_DM_REGISTER.invoke(watcher, DP_ENTITY_CUSTOM_NAME, customName);
        METHOD_DM_REGISTER.invoke(watcher, DP_ENTITY_HAS_CUSTOM_NAME, !customName.isEmpty());
    }

    private void setDWArmorStandStatus(@NotNull DataWatcher watcher, boolean small, boolean hasArms, boolean noBasePlate, boolean marker) {
        byte status = 0;
        status = setStatus(status, 0x1, small);
        status = setStatus(status, 0x4, hasArms);
        status = setStatus(status, 0x8, noBasePlate);
        status = setStatus(status, 0x10, marker);
        METHOD_DM_REGISTER.invoke(watcher, DP_ARMOR_STAND_STATUS, status);
    }

    private void setDWArmorStandHeadPose(@NotNull DataWatcher watcher, @NotNull EulerAngle pose) {
        METHOD_DM_REGISTER.invoke(watcher, DP_ARMOR_STAND_HEAD_ROTATION, toNMS(pose));
    }

    private void setDWArmorStandBodyPose(@NotNull DataWatcher watcher, @NotNull EulerAngle pose) {
        METHOD_DM_REGISTER.invoke(watcher, DP_ARMOR_STAND_BODY_ROTATION, toNMS(pose));
    }

    private void setDWArmorStandLeftArmPose(@NotNull DataWatcher watcher, @NotNull EulerAngle pose) {
        METHOD_DM_REGISTER.invoke(watcher, DP_ARMOR_STAND_LEFT_ARM_ROTATION, toNMS(pose));
    }

    private void setDWArmorStandRightArmPose(@NotNull DataWatcher watcher, @NotNull EulerAngle pose) {
        METHOD_DM_REGISTER.invoke(watcher, DP_ARMOR_STAND_RIGHT_ARM_ROTATION, toNMS(pose));
    }

    private void setDWArmorStandLeftLegPose(@NotNull DataWatcher watcher, @NotNull EulerAngle pose) {
        METHOD_DM_REGISTER.invoke(watcher, DP_ARMOR_STAND_LEFT_LEG_ROTATION, toNMS(pose));
    }

    private void setDWArmorStandRightLegPose(@NotNull DataWatcher watcher, @NotNull EulerAngle pose) {
        METHOD_DM_REGISTER.invoke(watcher, DP_ARMOR_STAND_RIGHT_LEG_ROTATION, toNMS(pose));
    }
}
