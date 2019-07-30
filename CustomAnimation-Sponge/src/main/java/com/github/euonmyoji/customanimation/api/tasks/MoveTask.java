package com.github.euonmyoji.customanimation.api.tasks;

import com.flowpowered.math.vector.Vector3d;
import com.github.euonmyoji.customanimation.CustomAnimation;
import com.github.euonmyoji.customanimation.util.Util;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author yinyangshi
 */
public class MoveTask extends AbstractLastTask {
    private volatile Future<Vector3d> nextTickV;
    private UUID worldUUID;
    private UUID playerUUID;
    private Vector3d endV;
    private Vector3d startV;

    public MoveTask(Player p, Location<World> endL, int tick) {
        super(tick);
        if (tick == 0) {
            p.setLocation(endL);
        } else {
            if(p.getWorld() != endL.getExtent()) {
                p.transferToWorld(endL.getExtent());
            }
            nextTickV = CustomAnimation.executorService.submit(() -> Util.get(startV, endV, (double) cur / tick));
            startV = p.getPosition();
            endV = endL.getPosition();
            worldUUID = endL.getExtent().getUniqueId();
        }
        playerUUID = p.getUniqueId();
    }

    @Override
    public UUID getPlayer() {
        return playerUUID;
    }

    @Override
    public boolean tick() {
        if (nextTickV != null) {
            Sponge.getServer().getPlayer(playerUUID).ifPresent(player -> {
                try {
                    player.setLocation(nextTickV.get(), worldUUID);
                } catch (InterruptedException | ExecutionException e) {
                    CustomAnimation.logger.warn("get move anime location error", e);
                }
            });
            if (cur++ < tick) {
                nextTickV = CustomAnimation.executorService.submit(() -> Util.get(startV, endV, (double) cur / tick));
                return false;
            }
        }
        return true;
    }
}
