package ru.violence.xholo.util;

import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.ArmorStandData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public enum UpdateFlag {
    STATUS((o, n) -> o.isSmall() != n.isSmall()
            || o.isHasArms() != n.isHasArms()
            || o.isHasBasePlate() != n.isHasBasePlate()
            || o.isMarker() != n.isMarker()),
    FLAGS((o, n) -> o.isVisible() != n.isVisible()),
    CUSTOM_NAME((o, n) -> !Objects.equals(o.getCustomName(), n.getCustomName())),
    BODY_POSE((o, n) -> !Objects.equals(o.getBodyPose(), n.getBodyPose())),
    LEFT_ARM_POSE((o, n) -> !Objects.equals(o.getLeftArmPose(), n.getLeftArmPose())),
    RIGHT_ARM_POSE((o, n) -> !Objects.equals(o.getRightArmPose(), n.getRightArmPose())),
    LEFT_LEG_POSE((o, n) -> !Objects.equals(o.getLeftLegPose(), n.getLeftLegPose())),
    RIGHT_LEG_POSE((o, n) -> !Objects.equals(o.getRightLegPose(), n.getRightLegPose())),
    HEAD_POSE((o, n) -> !Objects.equals(o.getHeadPose(), n.getHeadPose()));

    private static final UpdateFlag[] VALUES = values();
    private final @NotNull BiPredicate<ArmorStandData, ArmorStandData> changeChecker;

    UpdateFlag(@NotNull BiPredicate<ArmorStandData, ArmorStandData> changeChecker) {
        this.changeChecker = changeChecker;
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    public static @NotNull UpdateFlag[] compareUpdated(@NotNull ArmorStandData o, @NotNull ArmorStandData n) {
        List<UpdateFlag> list = new ArrayList<>(VALUES.length);

        for (UpdateFlag flag : VALUES) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        return list.toArray(new UpdateFlag[list.size()]);
    }

    public boolean isChanged(@NotNull ArmorStandData o, @NotNull ArmorStandData n) {
        return changeChecker.test(o, n);
    }
}
