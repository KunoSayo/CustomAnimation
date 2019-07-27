package com.github.euonmyoji.customanimation.tasks;

import com.flowpowered.math.vector.Vector3d;
import com.github.euonmyoji.customanimation.CustomAnimation;
import com.github.euonmyoji.customanimation.api.IAnimeTask;
import com.github.euonmyoji.customanimation.util.Util;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author yinyangshi
 */
public class MoveTask implements IAnimeTask {
    private volatile Future<Vector3d> nextTickV;
    private UUID worldUUID;
    private UUID playerUUID;
    private Vector3d endV;
    private Vector3d startV;
    private int tick;
    private int cur = 1;

    public MoveTask(Player p, Location<World> endL, int tick) {
        this.tick = tick;
        if (tick == 0) {
            p.setLocation(endL);
        } else if (tick == 1) {
            nextTickV = new FutureTask<>(endL::getPosition);
        } else {
            nextTickV = CustomAnimation.executorService.submit(() -> Util.get(startV, endV, (double) cur / tick));
            startV = p.getPosition();
            endV = endL.getPosition();
            worldUUID = endL.getExtent().getUniqueId();
        }
        playerUUID = p.getUniqueId();
    }

    @Override
    public boolean end() {
        return cur > tick;
    }

    @Override
    public UUID getPlayer() {
        return playerUUID;
    }

    @Override
    public void run() {
        Sponge.getServer().getPlayer(playerUUID).ifPresent(player -> {
            try {
                player.setLocation(nextTickV.get(), worldUUID);
            } catch (InterruptedException | ExecutionException e) {
                CustomAnimation.logger.warn("get move anime location error", e);
            }
        });
        if (cur++ < tick) {
            nextTickV = CustomAnimation.executorService.submit(() -> Util.get(startV, endV, (double) cur / tick));
        }
    }
}
