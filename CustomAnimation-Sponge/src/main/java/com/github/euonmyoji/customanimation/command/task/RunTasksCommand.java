package com.github.euonmyoji.customanimation.command.task;

import com.github.euonmyoji.customanimation.CustomAnimation;
import com.github.euonmyoji.customanimation.api.tasks.MoveTask;
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
 * @author yinyangshi
 */
public final class RunTasksCommand {

    public static final CommandSpec MOVE = CommandSpec.builder()
            .permission("customanimation.command.move")
            .arguments(GenericArguments.player(Text.of("player")), GenericArguments.location(Text.of("end")),
                    GenericArguments.integer(Text.of("tick")))
            .executor((src, args) -> {
                int tick = args.<Integer>getOne("tick").orElseThrow(NoSuchElementException::new);
                Collection<Player> players = args.getAll("playe r");
                Location<World> end = args.<Location<World>>getOne("end").orElseThrow(NoSuchElementException::new);
                if (tick < 1) {
                    throw new CommandException(Text.of("The tick should be positive!"));
                }
                for (Player player : players) {
                    CustomAnimation.ANIME_MANAGER.setTask(player.getUniqueId(), new MoveTask(player, end, tick));
                }
                return CommandResult.success();
            })
            .build();
}
