package ru.violence.xholo.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.util.nms.NMSUtil;

@UtilityClass
public class Utils {
    public static final double UNSET_DOUBLE = Double.NaN;
    public static final float UNSET_FLOAT = Float.NaN;

    public static boolean isUnset(double value) {
        return Double.isNaN(value);
    }

    public static boolean isUnset(float value) {
        return Float.isNaN(value);
    }

    @Contract(pure = true)
    public static boolean isInDisplayRange(@NotNull Player player, double x, double y, double z, double range) {
        double xDif = player.getX() - x;
        double yDif = player.getY() - y;
        double zDif = player.getZ() - z;

        double max = max(xDif, yDif, zDif);
        double min = min(xDif, yDif, zDif);

        double maxRange = Math.min(range, NMSUtil.getFurthestViewableBlock(player));

        return max <= maxRange &&
               min >= -maxRange;
    }

    @Contract(pure = true)
    public static double max(double d1, double d2, double d3) {
        double max = d1;
        max = Math.max(max, d2);
        max = Math.max(max, d3);
        return max;
    }

    @Contract(pure = true)
    public static double min(double d1, double d2, double d3) {
        double min = d1;
        min = Math.min(min, d2);
        min = Math.min(min, d3);
        return min;
    }
}
