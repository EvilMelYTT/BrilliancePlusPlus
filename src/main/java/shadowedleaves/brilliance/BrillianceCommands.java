package shadowedleaves.brilliance;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;
import net.minecraft.text.Text;

public class BrillianceCommands {
    private static final SuggestionProvider<FabricClientCommandSource> SUGGEST_STATES = (context, builder) -> {
        return net.minecraft.command.CommandSource.suggestMatching(new String[]{"on", "off"}, builder);
    };

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        registerCommand(dispatcher, "nightvision");
        registerCommand(dispatcher, "nv");
    }

    private static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, String name) {
        dispatcher.register(literal(name)
                .executes(context -> {
                    BrillianceClient.toggleNightVision();
                    context.getSource().sendFeedback(BrillianceClient.createToggleChatMessage());
                    return 1;
                })
                .then(argument("state", StringArgumentType.string())
                        .suggests(SUGGEST_STATES)
                        .executes(context -> {
                            String state = StringArgumentType.getString(context, "state");
                            if ("on".equalsIgnoreCase(state)) {
                                BrillianceClient.setNightVision(true);
                                context.getSource().sendFeedback(BrillianceClient.createChatMessage("on"));
                            } else if ("off".equalsIgnoreCase(state)) {
                                BrillianceClient.setNightVision(false);
                                context.getSource().sendFeedback(BrillianceClient.createChatMessage("off"));
                            } else {
                                context.getSource().sendFeedback(Text.literal("[Brilliance++] Unknown state: " + state));
                            }
                            return 1;
                        })
                )
        );
    }
}