package shadowedleaves.brilliance;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.lwjgl.glfw.GLFW;

public class BrillianceClient implements ClientModInitializer {
    public static boolean nightVision = false;
    private static KeyMapping toggleNightVisionKey;

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("brilliance", "general")
    );

    private static final TextColor BRILLIANCE_COLOR = TextColor.fromRgb(0xFFFF00); // Yellow
    private static final TextColor LIGHT_YELLOW_COLOR = TextColor.fromRgb(0xFFFF6E); // Light Yellow
    private static final TextColor GREEN_COLOR = TextColor.fromRgb(0x00FF00); // Green
    private static final TextColor RED_COLOR = TextColor.fromRgb(0xFF0000); // Red

    @Override
    public void onInitializeClient() {
        toggleNightVisionKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.brilliance.toggle_night_vision",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleNightVisionKey.consumeClick()) {
                toggleNightVision();
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            BrillianceCommands.register(dispatcher);
        });
    }

    public static void toggleNightVision() {
        nightVision = !nightVision;
        Minecraft client = Minecraft.getInstance();
        if (nightVision) {
            client.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        } else {
            client.player.removeEffect(MobEffects.NIGHT_VISION);
        }
        client.gui.hud.setOverlayMessage(createActionBarMessage(), true);
    }

    public static void setNightVision(boolean enable) {
        nightVision = enable;
        Minecraft client = Minecraft.getInstance();
        if (nightVision) {
            client.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        } else {
            client.player.removeEffect(MobEffects.NIGHT_VISION);
        }
        client.gui.hud.setOverlayMessage(createActionBarMessage(), true);
    }

    private static Component createActionBarMessage() {
        return Component.literal("Night Vision: ")
                .append(Component.literal(nightVision ? "ON" : "OFF")
                        .withStyle(style -> style.withColor(nightVision ? GREEN_COLOR : RED_COLOR)));
    }

    public static Component createChatMessage(String state) {
        return Component.literal("[Brilliance++] ")
                .withStyle(style -> style.withColor(BRILLIANCE_COLOR))
                .append(Component.literal("Set night vision to ")
                        .withStyle(style -> style.withColor(LIGHT_YELLOW_COLOR)))
                .append(Component.literal(state)
                        .withStyle(style -> style.withColor("on".equalsIgnoreCase(state) ? GREEN_COLOR : RED_COLOR)));
    }

    public static Component createToggleChatMessage() {
        return Component.literal("[Brilliance++] ")
                .withStyle(style -> style.withColor(BRILLIANCE_COLOR))
                .append(Component.literal("Toggled night vision!")
                        .withStyle(style -> style.withColor(LIGHT_YELLOW_COLOR)));
    }
}