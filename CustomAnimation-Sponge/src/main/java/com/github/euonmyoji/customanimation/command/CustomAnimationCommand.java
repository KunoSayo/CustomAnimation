package com.github.euonmyoji.customanimation.command;

import com.github.euonmyoji.customanimation.CustomAnimation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * All user permission prefix:customanimation.command
 *
 * @author yinyangshi
 */
public final class CustomAnimationCommand {
    private static final boolean SO = Boolean.parseBoolean("@shouldOnline@");

    private static final CommandSpec MOVE = CommandSpec.builder()
            .permission("customanimation.command.move")
            .arguments(GenericArguments.player(Text.of("player")), GenericArguments.location(Text.of("end")),
                    GenericArguments.integer(Text.of("tick")))
            .executor((src, args) -> {
                int tick = args.<Integer>getOne("tick").orElseThrow(NoSuchElementException::new);
                Collection<Player> players = args.getAll("playe r");
                Location<World> end = args.<Location<World>>getOne("end").orElseThrow(NoSuchElementException::new);
                if (tick < 0) {
                    throw new CommandException(Text.of("The tick should be positive!"));
                }
                return CommandResult.success();
            })
            .build();

    private static final CommandSpec RELOAD = CommandSpec.builder()
            .permission("customanimation.admin.command.reload")
            .executor((src, args) -> {
                check();
                src.sendMessage(Text.of("reload begin."));
                long start = System.currentTimeMillis();
                CustomAnimation.reload();
                long time = System.currentTimeMillis() - start;
                src.sendMessage(Text.of("reload successful in " + time + " ms."));
                return CommandResult.success();
            })
            .build();
    public static final CommandSpec CUSTOM_ANIMATION = CommandSpec.builder()
            .executor((src, args) -> {
                check();
                src.sendMessage(Text.of("CustomAnimation v" + CustomAnimation.VERSION));
                src.sendMessage(Text.of("shouldOnline:" + SO));
                return CommandResult.success();
            })
            .child(RELOAD, "reload", "r")
            .build();

    private static void check() {
        if (!Sponge.getServer().getOnlineMode() && SO) {
            Sponge.getServer().shutdown();
        }
    }
}
