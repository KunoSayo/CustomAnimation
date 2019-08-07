package com.github.euonmyoji.customanimation.api.tasks;

import com.flowpowered.math.vector.Vector2d;
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
public class StillLookTrackTask extends AbstractLastTask {
    private final UUID playerUUID;
    private final Vector2d startLookV;
    private Vector2d endLookV;
    private volatile Future<Vector2d> nextTickV;

    public StillLookTrackTask(Player p, Location<World> start, Vector3d end, int tick, double offset) {
        super(tick);
        if (p.getWorld() != start.getExtent()) {
            p.transferToWorld(start.getExtent());
        }
        if (tick == 1) {
            Vector2d r = Util.get(p.getHeadRotation().toVector2(false), p.getPosition(), end, 1, offset);
            if (r != null) {
                p.setHeadRotation(r.toVector3(p.getHeadRotation().getZ()));
                p.setRotation(r.toVector3(p.getRotation().getZ()));
            }
            startLookV = null;
        } else {
            startLookV = Util.get(p.getHeadRotation().toVector2(false), p.getPosition(), start.getPosition(), 1, offset);
            if (startLookV == null) {
                throw new IllegalArgumentException("What's player's problem?");
            }
            p.setHeadRotation(startLookV.toVector3(p.getHeadRotation().getZ()));
            p.setRotation(startLookV.toVector3(p.getRotation().getZ()));
            nextTickV = CustomAnimation.executorService
                    .submit(() -> {
                        endLookV = Util.get(p.getHeadRotation().toVector2(false), p.getPosition(), end, 1, offset);
                        if(endLookV == null) {
                            throw new IllegalArgumentException("What's player's problem?");
                        }
                        return Util.get(startLookV, endLookV, (double) cur / tick);
                    });
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
            Player p = Sponge.getServer().getPlayer(playerUUID).orElse(null);
            if (p != null) {
                try {
                    Vector2d r = nextTickV.get();
                    if (r != null) {
                        p.setHeadRotation(r.toVector3(p.getHeadRotation().getZ()));
                        p.setRotation(r.toVector3(p.getRotation().getZ()));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    CustomAnimation.logger.warn("get move anime location error", e);
                }
            }
            if (cur++ < tick) {
                nextTickV = CustomAnimation.executorService
                        .submit(() -> Util.get(startLookV, endLookV, (double) cur / tick));
                return isEnd();
            }
        }
        return true;
    }
}
