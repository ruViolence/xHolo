package ru.violence.xholo.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VirtualEntityBuilder {
    @Contract(pure = true)
    @Nullable Location location();

    @Contract(value = "_ -> this")
    @NotNull VirtualEntityBuilder location(@NotNull Location location);

    @Contract(pure = true)
    @Nullable World world();

    @Contract(value = "_ -> this")
    @NotNull VirtualEntityBuilder world(@NotNull World world);

    @Contract(pure = true)
    double x();

    @Contract(value = "_ -> this")
    @NotNull VirtualEntityBuilder x(double x);

    @Contract(pure = true)
    double y();

    @Contract(value = "_ -> this")
    @NotNull VirtualEntityBuilder y(double y);

    @Contract(pure = true)
    double z();

    @Contract(value = "_ -> this")
    @NotNull VirtualEntityBuilder z(double z);

    @Contract(pure = true)
    float yaw();

    @Contract(value = "_ -> this")
    @NotNull VirtualEntityBuilder yaw(float yaw);

    @Contract(pure = true)
    float pitch();

    @Contract(value = "_ -> this")
    @NotNull VirtualEntityBuilder pitch(float pitch);

    @Contract(value = "_, _, _, _, _ -> this")
    @NotNull VirtualEntityBuilder posRot(double x, double y, double z, float yaw, float pitch);
}
