package ru.violence.xholo.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.util.nms.NMSUtil;

@UtilityClass
public class Utils {
    @Contract(pure = true)
    public static boolean isInDisplayRange(@NotNull Player player, @NotNull Location loc, double range) {
        double xDif = player.getLocation().getX() - loc.getX();
        double yDif = player.getLocation().getY() - loc.getY();
        double zDif = player.getLocation().getZ() - loc.getZ();

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
