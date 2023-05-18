package ru.violence.xholo.api;

import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.xholo.api.impl.ArmorStandDataBuilderImpl;

public interface ArmorStandDataBuilder {
    @Contract(value = "-> new", pure = true)
    static @NotNull ArmorStandDataBuilder builder() {
        return new ArmorStandDataBuilderImpl();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ArmorStandDataBuilder from(@NotNull ArmorStandData data) {
        ArmorStandDataBuilderImpl armorStandDataBuilder = new ArmorStandDataBuilderImpl(data);
        armorStandDataBuilder.customName(null);
        return armorStandDataBuilder;
    }

    @Contract(pure = true)
    @Nullable CustomName customName();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder customName(@Nullable CustomName supplier);

    @Contract(pure = true)
    @Nullable EulerAngle bodyPose();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder bodyPose(@Nullable EulerAngle pose);

    @Contract(pure = true)
    @Nullable EulerAngle leftArmPose();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder leftArmPose(@Nullable EulerAngle pose);

    @Contract(pure = true)
    @Nullable EulerAngle rightArmPose();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder rightArmPose(@Nullable EulerAngle pose);

    @Contract(pure = true)
    @Nullable EulerAngle leftLegPose();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder leftLegPose(@Nullable EulerAngle pose);

    @Contract(pure = true)
    @Nullable EulerAngle rightLegPose();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder rightLegPose(@Nullable EulerAngle pose);

    @Contract(pure = true)
    @Nullable EulerAngle headPose();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder headPose(@Nullable EulerAngle pose);

    @Contract(pure = true)
    boolean hasBasePlate();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder hasBasePlate(boolean hasBasePlate);

    @Contract(pure = true)
    boolean visible();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder visible(boolean visible);

    @Contract(pure = true)
    boolean hasArms();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder hasArms(boolean hasArms);

    @Contract(pure = true)
    boolean small();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder small(boolean small);

    @Contract(pure = true)
    boolean marker();

    @Contract(value = "_ -> this")
    @NotNull ArmorStandDataBuilder marker(boolean marker);

    @Contract(value = "-> new", pure = true)
    @NotNull ArmorStandData build();
}
