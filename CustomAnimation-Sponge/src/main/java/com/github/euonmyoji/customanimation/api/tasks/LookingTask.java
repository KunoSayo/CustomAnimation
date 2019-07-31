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
public class LookingTask extends AbstractLastTask {
    private final double offset;
    private volatile Future<Vector2d> nextTickV;
    private UUID playerUUID;
    private Vector2d headV;
    private Vector3d pointV;
    private Vector3d startV;
    private final boolean useBig;

    public LookingTask(Player p, Location<World> l, int tick, double offset, boolean useBig) {
        super(tick);
        this.useBig = useBig;
        this.offset = offset;
        if (p.getWorld() != l.getExtent()) {
            p.transferToWorld(l.getExtent());
        }
        if (tick == 0) {
            Vector2d r = Util.get(headV, startV, pointV, 1, offset, useBig);
            if (r != null) {
                p.setHeadRotation(r.toVector3(p.getHeadRotation().getZ()));
            }
        } else {
            headV = p.getHeadRotation().toVector2();
            startV = p.getPosition();
            nextTickV = CustomAnimation.executorService
                    .submit(() -> Util.get(headV, startV, l.getPosition(), (double) cur / tick, offset, useBig));
            pointV = l.getPosition();
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
            Sponge.getServer().getPlayer(playerUUID).ifPresent(p -> {
                try {
                    Vector2d r = nextTickV.get();
                    if (r != null) {
                        p.setHeadRotation(r.toVector3(p.getHeadRotation().getZ()));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    CustomAnimation.logger.warn("get move anime location error", e);
                }
            });
            if (cur++ < tick) {
                nextTickV = CustomAnimation.executorService
                        .submit(() -> Util.get(headV, startV, pointV, (double) cur / tick, offset, useBig));
                return isEnd();
            }
        }
        return true;
    }
}
