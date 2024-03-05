package ru.violence.xholo.api.registry;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.xholo.api.VirtualEntity;

import java.util.List;

public interface HologramRegistry {
    @Contract(pure = true)
    @Nullable VirtualEntity getFromId(int entityId);
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull List<VirtualEntity> getAllFrom(@NotNull World world);

    @Contract(value = "-> new", pure = true)
    @NotNull List<VirtualEntity> getAll();

    @Contract(value = "_ -> new", pure = true)
    @NotNull List<VirtualEntity> getAllVisibleFor(@NotNull Player player);
}
