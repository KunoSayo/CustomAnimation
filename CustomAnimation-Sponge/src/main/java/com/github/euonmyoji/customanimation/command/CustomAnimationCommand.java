package com.github.euonmyoji.customanimation.command;

import com.github.euonmyoji.customanimation.CustomAnimation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * All user permission prefix:customanimation.command
 *
 * @author yinyangshi
 */
public final class CustomAnimationCommand {
    private static final boolean SO = Boolean.parseBoolean("@shouldOnline@");

    private static final CommandSpec RUN_ANIME = CommandSpec.builder()
            .executor(CustomAnimationCommand::showVersion)
            .build();

    private static final CommandSpec RUN_TASK = CommandSpec.builder()
            .permission("customanimation.admin.command.runtask")
            .executor(CustomAnimationCommand::showVersion)
            .child(RunTasksCommand.STILL_LOOK_TRACK, "stilllooktrack", "stilllook")
            .child(RunTasksCommand.MOVE, "movetask", "move")
            .build();

    private static final CommandSpec RELOAD = CommandSpec.builder()
            .permission("customanimation.admin.command.reload")
            .executor((src, args) -> {
                check();
                src.sendMessage(Text.of("reload begin."));
                long start = System.currentTimeMillis();
                CustomAnimation.reload(src);
                long time = System.currentTimeMillis() - start;
                src.sendMessage(Text.of("reload successful in " + time + " ms."));
                return CommandResult.success();
            })
            .build();

    private static final CommandSpec LIST = CommandSpec.builder()
            .permission("customanimation.admin.command.list")
            .executor((src, args) -> {
                return CommandResult.success();
            })
            .build();

    public static final CommandSpec CUSTOM_ANIMATION = CommandSpec.builder()
            .executor(CustomAnimationCommand::showVersion)
            .child(RELOAD, "reload", "r")
            .child(RUN_ANIME, "runanime", "ra", "anime")
            .child(RUN_TASK, "runtask", "rt", "task")
            .child(LIST, "list")
            .build();


    private static void check() {
        if (!Sponge.getServer().getOnlineMode() && SO) {
            Sponge.getServer().shutdown();
        }
    }

    private static CommandResult showVersion(CommandSource src, CommandContext args) {
        check();
        src.sendMessage(Text.of("CustomAnimation v" + CustomAnimation.VERSION));
        src.sendMessage(Text.of("shouldOnline:" + SO));
        src.sendMessage(Text.of("For help: /customanimation help"));
        return CommandResult.success();
    }
}
