package com.github.euonmyoji.customanimation.tasks;

import com.flowpowered.math.vector.Vector2d;
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

/**
 * @author yinyangshi
 */
public class LookingTask implements IAnimeTask {
    private volatile Future<Vector2d> nextTickV;
    private UUID playerUUID;
    private Vector2d headV;
    private Vector3d pointV;
    private Vector3d startV;
    private int tick;
    private int cur = 1;

    public LookingTask(Player p, Location<World> l, int tick) {
        this.tick = tick;
        if (tick == 0) {
            if (p.getWorld() != l.getExtent()) {
                p.transferToWorld(l.getExtent());
            }
            p.lookAt(l.getPosition());
        } else {
            headV = p.getHeadRotation().toVector2();
            startV = p.getPosition();
            nextTickV = CustomAnimation.executorService
                    .submit(() -> Util.get(headV, startV, l.getPosition(), (double) cur / tick));
            pointV = l.getPosition();
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
        Sponge.getServer().getPlayer(playerUUID).ifPresent(p -> {
            try {
                p.setHeadRotation(nextTickV.get().toVector3(p.getHeadRotation().getZ()));
            } catch (InterruptedException | ExecutionException e) {
                CustomAnimation.logger.warn("get move anime location error", e);
            }
        });
        if (cur++ < tick) {
            nextTickV = CustomAnimation.executorService
                    .submit(() -> Util.get(headV, startV, pointV, (double) cur / tick));
        }
    }
}
