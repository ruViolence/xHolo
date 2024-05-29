package ru.violence.xholo.util.updateflags;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import ru.violence.xholo.api.ArmorStandData;
import ru.violence.xholo.api.BlockDisplayData;
import ru.violence.xholo.api.DisplayData;
import ru.violence.xholo.api.InteractionData;
import ru.violence.xholo.api.ItemDisplayData;
import ru.violence.xholo.api.TextDisplayData;
import ru.violence.xholo.api.impl.DisplayDataImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class UpdateFlags {
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_STATUS = new UpdateFlag<>(0) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return o.isSmall() != n.isSmall()
                   || o.isHasArms() != n.isHasArms()
                   || o.isHasBasePlate() != n.isHasBasePlate()
                   || o.isMarker() != n.isMarker();
        }
    };
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_FLAGS = new UpdateFlag<>(1) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return o.isVisible() != n.isVisible();
        }
    };
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_CUSTOM_NAME = new UpdateFlag<>(2) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return !Objects.equals(o.getCustomName(), n.getCustomName());
        }
    };
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_BODY_POSE = new UpdateFlag<>(3) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return !Objects.equals(o.getBodyPose(), n.getBodyPose());
        }
    };
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_LEFT_ARM_POSE = new UpdateFlag<>(4) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return !Objects.equals(o.getLeftArmPose(), n.getLeftArmPose());
        }
    };
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_RIGHT_ARM_POSE = new UpdateFlag<>(5) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return !Objects.equals(o.getRightArmPose(), n.getRightArmPose());
        }
    };
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_LEFT_LEG_POSE = new UpdateFlag<>(6) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return !Objects.equals(o.getLeftLegPose(), n.getLeftLegPose());
        }
    };
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_RIGHT_LEG_POSE = new UpdateFlag<>(7) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return !Objects.equals(o.getRightLegPose(), n.getRightLegPose());
        }
    };
    public final UpdateFlag<ArmorStandData> ARMOR_STAND_HEAD_POSE = new UpdateFlag<>(8) {
        @Override
        public boolean isChanged(ArmorStandData o, ArmorStandData n) {
            return !Objects.equals(o.getHeadPose(), n.getHeadPose());
        }
    };

    public final UpdateFlag<DisplayData> DISPLAY_TRANSFORMATION = new UpdateFlag<>(9) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return !Objects.equals(o.getTransformation(), n.getTransformation());
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_INTERPOLATION_DURATION = new UpdateFlag<>(10) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return o.getInterpolationDuration() != n.getInterpolationDuration();
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_TELEPORT_DURATION = new UpdateFlag<>(11) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return o.getTeleportDuration() != n.getTeleportDuration();
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_SHADOW_RADIUS = new UpdateFlag<>(12) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return o.getShadowRadius() != n.getShadowRadius();
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_SHADOW_STRENGTH = new UpdateFlag<>(13) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return o.getShadowStrength() != n.getShadowStrength();
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_WIDTH = new UpdateFlag<>(14) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return o.getDisplayWidth() != n.getDisplayWidth();
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_HEIGHT = new UpdateFlag<>(15) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return o.getDisplayHeight() != n.getDisplayHeight();
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_INTERPOLATION_DELAY = new UpdateFlag<>(16) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return !((DisplayDataImpl) o).getUniqueInterpolationDelay().equals(((DisplayDataImpl) n).getUniqueInterpolationDelay());
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_BILLBOARD = new UpdateFlag<>(17) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return !o.getBillboard().equals(n.getBillboard());
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_GLOW_COLOR_OVERRIDE = new UpdateFlag<>(18) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return !Objects.equals(o.getGlowColorOverride(), n.getGlowColorOverride());
        }
    };
    public final UpdateFlag<DisplayData> DISPLAY_BRIGHTNESS = new UpdateFlag<>(19) {
        @Override
        public boolean isChanged(DisplayData o, DisplayData n) {
            return !Objects.equals(o.getBrightness(), n.getBrightness());
        }
    };

    public final UpdateFlag<BlockDisplayData> BLOCK_DISPLAY_BLOCK = new UpdateFlag<>(20) {
        @Override
        public boolean isChanged(BlockDisplayData o, BlockDisplayData n) {
            return !o.getBlock().equals(n.getBlock());
        }
    };

    public final UpdateFlag<ItemDisplayData> ITEM_DISPLAY_ITEM = new UpdateFlag<>(21) {
        @Override
        public boolean isChanged(ItemDisplayData o, ItemDisplayData n) {
            return !Objects.equals(o.getItem(), n.getItem());
        }
    };
    public final UpdateFlag<ItemDisplayData> ITEM_DISPLAY_TRANSFORM = new UpdateFlag<>(22) {
        @Override
        public boolean isChanged(ItemDisplayData o, ItemDisplayData n) {
            return o.getDisplayTransform().equals(n.getDisplayTransform());
        }
    };

    public final UpdateFlag<TextDisplayData> TEXT_DISPLAY_TEXT = new UpdateFlag<>(23) {
        @Override
        public boolean isChanged(TextDisplayData o, TextDisplayData n) {
            return !o.getText().equals(n.getText());
        }
    };
    public final UpdateFlag<TextDisplayData> TEXT_DISPLAY_LINE_WIDTH = new UpdateFlag<>(24) {
        @Override
        public boolean isChanged(TextDisplayData o, TextDisplayData n) {
            return o.getLineWidth() != n.getLineWidth();
        }
    };
    public final UpdateFlag<TextDisplayData> TEXT_DISPLAY_BACKGROUND_COLOR = new UpdateFlag<>(25) {
        @Override
        public boolean isChanged(TextDisplayData o, TextDisplayData n) {
            return !Objects.equals(o.getBackgroundColor(), n.getBackgroundColor());
        }
    };
    public final UpdateFlag<TextDisplayData> TEXT_DISPLAY_OPACITY = new UpdateFlag<>(26) {
        @Override
        public boolean isChanged(TextDisplayData o, TextDisplayData n) {
            return o.getTextOpacity() != n.getTextOpacity();
        }
    };
    public final UpdateFlag<TextDisplayData> TEXT_DISPLAY_STYLE = new UpdateFlag<>(27) {
        @Override
        public boolean isChanged(TextDisplayData o, TextDisplayData n) {
            return o.isShadowed() != n.isShadowed() ||
                   o.isSeeThrough() != n.isSeeThrough() ||
                   o.isDefaultBackground() != n.isDefaultBackground() ||
                   o.getAlignment().equals(n.getAlignment());
        }
    };

    public final UpdateFlag<InteractionData> INTERACTION_WIDTH = new UpdateFlag<>(28) {
        @Override
        public boolean isChanged(InteractionData o, InteractionData n) {
            return o.getWidth() != n.getWidth();
        }
    };
    public final UpdateFlag<InteractionData> INTERACTION_HEIGHT = new UpdateFlag<>(29) {
        @Override
        public boolean isChanged(InteractionData o, InteractionData n) {
            return o.getHeight() != n.getHeight();
        }
    };
    public final UpdateFlag<InteractionData> INTERACTION_RESPONSE = new UpdateFlag<>(30) {
        @Override
        public boolean isChanged(InteractionData o, InteractionData n) {
            return o.isResponsive() != n.isResponsive();
        }
    };

    public final List<UpdateFlag<ArmorStandData>> ALL_ARMOR_STAND_FLAGS = List.of(
            ARMOR_STAND_STATUS,
            ARMOR_STAND_FLAGS,
            ARMOR_STAND_CUSTOM_NAME,
            ARMOR_STAND_BODY_POSE,
            ARMOR_STAND_LEFT_ARM_POSE,
            ARMOR_STAND_RIGHT_ARM_POSE,
            ARMOR_STAND_LEFT_LEG_POSE,
            ARMOR_STAND_RIGHT_LEG_POSE,
            ARMOR_STAND_HEAD_POSE
    );

    public final List<UpdateFlag<DisplayData>> ALL_DISPLAY_FLAGS = List.of(
            DISPLAY_TRANSFORMATION,
            DISPLAY_INTERPOLATION_DURATION,
            DISPLAY_TELEPORT_DURATION,
            DISPLAY_SHADOW_RADIUS,
            DISPLAY_SHADOW_STRENGTH,
            DISPLAY_WIDTH,
            DISPLAY_HEIGHT,
            DISPLAY_INTERPOLATION_DELAY,
            DISPLAY_BILLBOARD,
            DISPLAY_GLOW_COLOR_OVERRIDE,
            DISPLAY_BRIGHTNESS
    );

    public final List<UpdateFlag<BlockDisplayData>> ALL_BLOCK_DISPLAY_FLAGS = List.of(
            BLOCK_DISPLAY_BLOCK
    );

    public final List<UpdateFlag<ItemDisplayData>> ALL_ITEM_DISPLAY_FLAGS = List.of(
            ITEM_DISPLAY_ITEM,
            ITEM_DISPLAY_TRANSFORM
    );

    public final List<UpdateFlag<TextDisplayData>> ALL_TEXT_DISPLAY_FLAGS = List.of(
            TEXT_DISPLAY_TEXT,
            TEXT_DISPLAY_LINE_WIDTH,
            TEXT_DISPLAY_BACKGROUND_COLOR,
            TEXT_DISPLAY_OPACITY,
            TEXT_DISPLAY_STYLE
    );

    public final List<UpdateFlag<InteractionData>> ALL_INTERACTION_FLAGS = List.of(
            INTERACTION_WIDTH,
            INTERACTION_HEIGHT,
            INTERACTION_RESPONSE
    );

    public @NotNull List<UpdateFlag<?>> compareArmorStandData(@NotNull ArmorStandData o, @NotNull ArmorStandData n) {
        List<UpdateFlag<?>> list = new ArrayList<>(9);

        for (UpdateFlag<ArmorStandData> flag : ALL_ARMOR_STAND_FLAGS) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        return list;
    }

    public @NotNull List<UpdateFlag<?>> compareBlockDisplayData(@NotNull BlockDisplayData o, @NotNull BlockDisplayData n) {
        List<UpdateFlag<?>> list = new ArrayList<>(9);

        for (UpdateFlag<DisplayData> flag : ALL_DISPLAY_FLAGS) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        for (UpdateFlag<BlockDisplayData> flag : ALL_BLOCK_DISPLAY_FLAGS) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        return list;
    }

    public @NotNull List<UpdateFlag<?>> compareItemDisplayData(@NotNull ItemDisplayData o, @NotNull ItemDisplayData n) {
        List<UpdateFlag<?>> list = new ArrayList<>(9);

        for (UpdateFlag<DisplayData> flag : ALL_DISPLAY_FLAGS) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        for (UpdateFlag<ItemDisplayData> flag : ALL_ITEM_DISPLAY_FLAGS) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        return list;
    }

    public @NotNull List<UpdateFlag<?>> compareTextDisplayData(@NotNull TextDisplayData o, @NotNull TextDisplayData n) {
        List<UpdateFlag<?>> list = new ArrayList<>(9);

        for (UpdateFlag<DisplayData> flag : ALL_DISPLAY_FLAGS) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        for (UpdateFlag<TextDisplayData> flag : ALL_TEXT_DISPLAY_FLAGS) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        return list;
    }

    public @NotNull List<UpdateFlag<?>> compareInteractionData(@NotNull InteractionData o, @NotNull InteractionData n) {
        List<UpdateFlag<?>> list = new ArrayList<>(9);

        for (UpdateFlag<InteractionData> flag : ALL_INTERACTION_FLAGS) {
            if (flag.isChanged(o, n)) {
                list.add(flag);
            }
        }

        return list;
    }
}
