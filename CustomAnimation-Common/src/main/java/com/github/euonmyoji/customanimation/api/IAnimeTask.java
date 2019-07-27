package com.github.euonmyoji.customanimation.api;

import java.util.UUID;

/**
 * the run will be invoked in tick
 * the task can contain child task(s)
 * @author yinyangshi
 */
public interface IAnimeTask extends Runnable {
    /**
     * if the anime is end
     *
     * @return true if end
     */
    boolean end();

    /**
     * get the uuid of the player playing
     *
     * @return the uuid of the player
     */
    UUID getPlayer();
}
