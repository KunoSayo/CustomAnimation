package com.github.euonmyoji.customanimation.api.tasks;

import com.github.euonmyoji.customanimation.CustomAnimation;
import com.github.euonmyoji.customanimation.util.TextUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.TextTemplate;

import java.util.List;
import java.util.UUID;

/**
 * @author yinyangshi
 */
public class CommandTask extends AbstractLastTask {
    private final List<TextTemplate> commands;
    private UUID playerUUID;

    public CommandTask(UUID playerUUID, List<TextTemplate> commands, int tick) {
        super(tick);
        this.commands = commands;
        this.playerUUID = playerUUID;
    }

    @Override
    public boolean isEnd() {
        return cur > tick;
    }

    @Override
    public UUID getPlayer() {
        return playerUUID;
    }

    @Override
    public boolean tick() {
        if (cur++ < tick) {
            Sponge.getServer().getPlayer(playerUUID).ifPresent(player -> {
                commands.stream().map(textTemplate -> TextUtil.parse(player, textTemplate))
                        .forEach(s -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), s));
            });
            return isEnd();
        } else {
            CustomAnimation.logger.debug("Why run here", new Exception("Why run here"));
        }
        return true;
    }

    @Override
    public boolean endAnime() {
        return false;
    }
}
