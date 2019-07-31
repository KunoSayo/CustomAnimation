package com.github.euonmyoji.customanimation.command;

import com.github.euonmyoji.customanimation.CustomAnimation;
import com.github.euonmyoji.customanimation.api.tasks.MoveTask;
import com.github.euonmyoji.customanimation.api.tasks.StillLookTrackTask;
import com.github.euonmyoji.customanimation.util.TextUtil;
import com.github.euonmyoji.customanimation.util.Util;
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
final class RunTasksCommand {
    static final CommandSpec STILL_LOOK_TRACK = CommandSpec.builder()
            .permission("customanimation.admin.command.looktrack")
            .arguments(GenericArguments.player(Text.of("player")), GenericArguments.location(Text.of("start")),
                    GenericArguments.location(Text.of("end")),
                    GenericArguments.integer(Text.of("tick")),
                    GenericArguments.optional(GenericArguments.doubleNum(Text.of("offset"))))
            .executor((src, args) -> {
                int tick = args.<Integer>getOne("tick").orElseThrow(NoSuchElementException::new);
                Collection<Player> players = args.getAll("player");
                Location<World> start = args.<Location<World>>getOne("start").orElseThrow(NoSuchElementException::new);
                Location<World> end = args.<Location<World>>getOne("end").orElseThrow(NoSuchElementException::new);
                if (tick < 1) {
                    throw new CommandException(Text.of("The tick should be positive!"));
                }
                if (start.getExtent() != end.getExtent()) {
                    throw new CommandException(Text.of("The start point and end point is not the same world!"));
                }
                double offset = args.<Double>getOne("offset").orElse(0.0);
                if (offset != 0) {
                    if (offset % 180 == 0) {
                        offset = Math.PI * (offset / 180);
                    } else {
                        offset *= Util.UNIT_RAD;
                    }
                }
                for (Player player : players) {
                    if (CustomAnimation.ANIME_MANAGER.setTask(player.getUniqueId(), new StillLookTrackTask(player, start, end.getPosition(), tick, offset))) {
                        src.sendMessage(TextUtil.toText(CustomAnimation.RAW_TEXT_MANAGER.get("customanimation.command.setPlayerTask.successful", src.getLocale())
                                .replace("{player_name}", player.getName())));
                    } else {
                        src.sendMessage(TextUtil.toText(CustomAnimation.RAW_TEXT_MANAGER.get("customanimation.command.setPlayerTask.failed", src.getLocale())
                                .replace("{player_name}", player.getName())));
                    }
                }
                return CommandResult.success();
            })
            .build();

    static final CommandSpec MOVE = CommandSpec.builder()
            .permission("customanimation.admin.command.move")
            .arguments(GenericArguments.player(Text.of("player")), GenericArguments.location(Text.of("end")),
                    GenericArguments.integer(Text.of("tick")))
            .executor((src, args) -> {
                int tick = args.<Integer>getOne("tick").orElseThrow(NoSuchElementException::new);
                Collection<Player> players = args.getAll("player");
                Location<World> end = args.<Location<World>>getOne("end").orElseThrow(NoSuchElementException::new);
                if (tick < 1) {
                    throw new CommandException(Text.of("The tick should be positive!"));
                }
                for (Player player : players) {
                    if (CustomAnimation.ANIME_MANAGER.setTask(player.getUniqueId(), new MoveTask(player, end, tick))) {
                        src.sendMessage(Text.of("Set player " + player.getName() + " anime task successful."));
                    } else {
                        src.sendMessage(Text.of("Set player " + player.getName() + " anime task failed!"));
                    }
                }
                return CommandResult.success();
            })
            .build();
}
