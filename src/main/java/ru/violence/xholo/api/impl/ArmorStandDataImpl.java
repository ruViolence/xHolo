package ru.violence.xholo.api.impl;

import lombok.Getter;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.ArmorStandDataBuilder;
import ru.violence.xholo.api.CustomName;

@Getter
public final class ArmorStandDataImpl implements ArmorStandData {
    private final @Nullable CustomName customName;
    private final @Nullable EulerAngle bodyPose;
    private final @Nullable EulerAngle leftArmPose;
    private final @Nullable EulerAngle rightArmPose;
    private final @Nullable EulerAngle leftLegPose;
    private final @Nullable EulerAngle rightLegPose;
    private final @Nullable EulerAngle headPose;
    private final boolean hasBasePlate;
    private final boolean visible;
    private final boolean hasArms;
    private final boolean small;
    private final boolean marker;

    public ArmorStandDataImpl(@Nullable CustomName customName,
                              @Nullable EulerAngle bodyPose,
                              @Nullable EulerAngle leftArmPose,
                              @Nullable EulerAngle rightArmPose,
                              @Nullable EulerAngle leftLegPose,
                              @Nullable EulerAngle rightLegPose,
                              @Nullable EulerAngle headPose,
                              boolean hasBasePlate,
                              boolean visible,
                              boolean hasArms,
                              boolean small,
                              boolean marker) {
        this.customName = customName;
        this.bodyPose = bodyPose;
        this.leftArmPose = leftArmPose;
        this.rightArmPose = rightArmPose;
        this.leftLegPose = leftLegPose;
        this.rightLegPose = rightLegPose;
        this.headPose = headPose;
        this.hasBasePlate = hasBasePlate;
        this.visible = visible;
        this.hasArms = hasArms;
        this.small = small;
        this.marker = marker;
    }

    @Override
    public @NotNull ArmorStandDataBuilder modify() {
        return new ArmorStandDataBuilderImpl(this);
    }
}
