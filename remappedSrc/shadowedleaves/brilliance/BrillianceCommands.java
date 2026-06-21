package shadowedleaves.brilliance;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class BrillianceCommands {
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_STATES = (context, builder) -> {
        return net.minecraft.commands.SharedSuggestionProvider.suggest(new String[]{"on", "off"}, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nightvision")
                .executes(context -> {
                    BrillianceClient.toggleNightVision();
                    context.getSource().sendSuccess(() -> BrillianceClient.createToggleChatMessage(), false);
                    return 1;
                })
                .then(Commands.argument("state", StringArgumentType.string())
                        .suggests(SUGGEST_STATES)
                        .executes(context -> {
                            String state = StringArgumentType.getString(context, "state");
                            if ("on".equalsIgnoreCase(state)) {
                                BrillianceClient.setNightVision(true);
                                context.getSource().sendSuccess(() -> BrillianceClient.createChatMessage("on"), false);
                            } else if ("off".equalsIgnoreCase(state)) {
                                BrillianceClient.setNightVision(false);
                                context.getSource().sendSuccess(() -> BrillianceClient.createChatMessage("off"), false);
                            } else {
                                context.getSource().sendSuccess(() -> Component.literal("[Brilliance] Unknown state: " + state), false);
                            }
                            return 1;
                        })
                )
        );
    }
}