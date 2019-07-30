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
public class LookTrackTask extends AbstractLastTask {
    private final UUID playerUUID;
    private final Vector2d headV;
    private final Vector3d pointStart;
    private final Vector3d pointEnd;
    private final double offset;
    private volatile Future<Vector2d> nextTickV;

    public LookTrackTask(Player p, Location<World> start, Vector3d end, int tick, double offset) {
        super(tick);
        if (p.getWorld() != start.getExtent()) {
            p.transferToWorld(start.getExtent());
        }
        this.headV = p.getHeadRotation().toVector2();
        pointStart = start.getPosition();
        pointEnd = end;
        if (tick == 1) {
            Vector2d r = Util.get(headV, p.getPosition(), Util.get(pointStart, pointEnd, (double) cur / tick), 1, offset);
            if (r != null) {
                p.setHeadRotation(r.toVector3(p.getHeadRotation().getZ()));
            }
        } else {
            nextTickV = CustomAnimation.executorService
                    .submit(() -> Util.get(headV, p.getPosition(), Util.get(pointStart, pointEnd, (double) cur / tick), 1, offset));
        }
        playerUUID = p.getUniqueId();
        this.offset = offset;
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
                    p.setHeadRotation(nextTickV.get().toVector3(p.getHeadRotation().getZ()));
                } catch (InterruptedException | ExecutionException e) {
                    CustomAnimation.logger.warn("get move anime location error", e);
                }
            }
            if (cur++ < tick) {
                if (p != null) {
                    nextTickV = CustomAnimation.executorService
                            .submit(() -> Util.get(headV, p.getPosition(), Util.get(pointStart, pointEnd, (double) cur / tick), 1, offset));
                }
                return isEnd();
            }
        }
        return true;
    }
}
