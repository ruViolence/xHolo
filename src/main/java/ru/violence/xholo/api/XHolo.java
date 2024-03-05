package ru.violence.xholo.api;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.XHoloPlugin;
import ru.violence.xholo.api.impl.VirtualArmorStandBuilderImpl;
import ru.violence.xholo.api.impl.VirtualBlockDisplayBuilderImpl;
import ru.violence.xholo.api.impl.VirtualInteractionBuilderImpl;
import ru.violence.xholo.api.impl.VirtualItemDisplayBuilderImpl;
import ru.violence.xholo.api.impl.VirtualTextDisplayBuilderImpl;
import ru.violence.xholo.api.registry.HologramRegistry;

@UtilityClass
public class XHolo {
    @Contract(pure = true)
    public static @NotNull HologramRegistry getRegistry() {
        return XHoloPlugin.getInstance().getRegistry();
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull VirtualArmorStandBuilder armorStand(@NotNull Plugin plugin) {
        return new VirtualArmorStandBuilderImpl(plugin);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull VirtualBlockDisplayBuilder blockDisplay(@NotNull Plugin plugin) {
        return new VirtualBlockDisplayBuilderImpl(plugin);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull VirtualItemDisplayBuilder itemDisplay(@NotNull Plugin plugin) {
        return new VirtualItemDisplayBuilderImpl(plugin);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull VirtualTextDisplayBuilder textDisplay(@NotNull Plugin plugin) {
        return new VirtualTextDisplayBuilderImpl(plugin);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull VirtualInteractionBuilder interaction(@NotNull Plugin plugin) {
        return new VirtualInteractionBuilderImpl(plugin);
    }
}
