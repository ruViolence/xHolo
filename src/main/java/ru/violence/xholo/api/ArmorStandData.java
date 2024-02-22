package ru.violence.xholo.api;

import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ArmorStandData extends VirtualEntityData {
    @Contract(pure = true)
    @Nullable CustomName getCustomName();

    @Contract(pure = true)
    @Nullable EulerAngle getBodyPose();

    @Contract(pure = true)
    @Nullable EulerAngle getLeftArmPose();

    @Contract(pure = true)
    @Nullable EulerAngle getRightArmPose();

    @Contract(pure = true)
    @Nullable EulerAngle getLeftLegPose();

    @Contract(pure = true)
    @Nullable EulerAngle getRightLegPose();

    @Contract(pure = true)
    @Nullable EulerAngle getHeadPose();

    @Contract(pure = true)
    boolean isHasBasePlate();

    @Contract(pure = true)
    boolean isHasArms();

    @Contract(pure = true)
    boolean isSmall();

    @Contract(pure = true)
    boolean isMarker();

    @Contract(pure = true)
    @NotNull ArmorStandDataBuilder modify();
}
