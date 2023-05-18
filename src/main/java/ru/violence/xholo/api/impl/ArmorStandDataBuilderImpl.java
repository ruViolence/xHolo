package ru.violence.xholo.api.impl;

import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.util.Check;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.ArmorStandDataBuilder;
import ru.violence.xholo.api.CustomName;

public final class ArmorStandDataBuilderImpl implements ArmorStandDataBuilder {
    private @Nullable CustomName customName;
    private @Nullable EulerAngle bodyPose;
    private @Nullable EulerAngle leftArmPose;
    private @Nullable EulerAngle rightArmPose;
    private @Nullable EulerAngle leftLegPose;
    private @Nullable EulerAngle rightLegPose;
    private @Nullable EulerAngle headPose;
    private boolean hasBasePlate;
    private boolean visible;
    private boolean hasArms;
    private boolean small;
    private boolean marker;

    public ArmorStandDataBuilderImpl() {

    }

    public ArmorStandDataBuilderImpl(@NotNull ArmorStandData data) {
        Check.notNull(data, "Data is null");
        this.customName = data.getCustomName();
        this.bodyPose = data.getBodyPose();
        this.leftArmPose = data.getLeftArmPose();
        this.rightArmPose = data.getRightArmPose();
        this.leftLegPose = data.getLeftLegPose();
        this.rightLegPose = data.getRightLegPose();
        this.headPose = data.getHeadPose();
        this.hasBasePlate = data.isHasBasePlate();
        this.visible = data.isVisible();
        this.hasArms = data.isHasArms();
        this.small = data.isSmall();
        this.marker = data.isMarker();
    }

    @Override
    public @Nullable CustomName customName() {
        return customName;
    }

    @Override
    public @NotNull ArmorStandDataBuilder customName(@Nullable CustomName name) {
        this.customName = name;
        return this;
    }

    @Override
    public @Nullable EulerAngle bodyPose() {
        return bodyPose;
    }

    @Override
    public @NotNull ArmorStandDataBuilder bodyPose(@Nullable EulerAngle pose) {
        this.bodyPose = pose;
        return this;
    }

    @Override
    public @Nullable EulerAngle leftArmPose() {
        return leftArmPose;
    }

    @Override
    public @NotNull ArmorStandDataBuilder leftArmPose(@Nullable EulerAngle pose) {
        this.leftArmPose = pose;
        return this;
    }

    @Override
    public @Nullable EulerAngle rightArmPose() {
        return rightArmPose;
    }

    @Override
    public @NotNull ArmorStandDataBuilder rightArmPose(@Nullable EulerAngle pose) {
        this.rightArmPose = pose;
        return this;
    }

    @Override
    public @Nullable EulerAngle leftLegPose() {
        return leftLegPose;
    }

    @Override
    public @NotNull ArmorStandDataBuilder leftLegPose(@Nullable EulerAngle pose) {
        this.leftLegPose = pose;
        return this;
    }

    @Override
    public @Nullable EulerAngle rightLegPose() {
        return rightLegPose;
    }

    @Override
    public @NotNull ArmorStandDataBuilder rightLegPose(@Nullable EulerAngle pose) {
        this.rightLegPose = pose;
        return this;
    }

    @Override
    public @Nullable EulerAngle headPose() {
        return headPose;
    }

    @Override
    public @NotNull ArmorStandDataBuilder headPose(@Nullable EulerAngle pose) {
        this.headPose = pose;
        return this;
    }

    @Override
    public boolean hasBasePlate() {
        return hasBasePlate;
    }

    @Override
    public @NotNull ArmorStandDataBuilder hasBasePlate(boolean hasBasePlate) {
        this.hasBasePlate = hasBasePlate;
        return this;
    }

    @Override
    public boolean visible() {
        return visible;
    }

    @Override
    public @NotNull ArmorStandDataBuilder visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public boolean hasArms() {
        return hasArms;
    }

    @Override
    public @NotNull ArmorStandDataBuilder hasArms(boolean hasArms) {
        this.hasArms = hasArms;
        return this;
    }

    @Override
    public boolean small() {
        return small;
    }

    @Override
    public @NotNull ArmorStandDataBuilder small(boolean small) {
        this.small = small;
        return this;
    }

    @Override
    public boolean marker() {
        return marker;
    }

    @Override
    public @NotNull ArmorStandDataBuilder marker(boolean marker) {
        this.marker = marker;
        return this;
    }

    @Override
    public @NotNull ArmorStandData build() {
        return new ArmorStandDataImpl(
                customName,
                bodyPose,
                leftArmPose,
                rightArmPose,
                leftLegPose,
                rightLegPose,
                headPose,
                hasBasePlate,
                visible,
                hasArms,
                small,
                marker
        );
    }
}
