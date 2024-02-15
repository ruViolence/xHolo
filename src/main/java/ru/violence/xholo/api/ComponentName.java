package ru.violence.xholo.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.violence.coreapi.common.api.util.Check;

public class ComponentName implements CustomName {
    private final @NotNull Component component;

    ComponentName(@NotNull Component component) {
        this.component = Check.notNull(component, "Component is null");
    }

    @Override
    @Contract(pure = true)
    public @NotNull Component apply(@Nullable Player player) {
        return component;
    }

    @Contract(pure = true)
    public @NotNull Component getComponent() {
        return component;
    }
}
