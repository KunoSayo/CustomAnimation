package com.github.euonmyoji.customanimation.api.tasks;

import java.util.UUID;

/**
 * the tick will be invoked in minecraft tick
 * the task can contain child task(s)
 * @author yinyangshi
 */
public interface IAnimeTask {
    /**
     * if the anime is end
     *
     * @return true if end
     */
    boolean isEnd();

    /**
     * get the uuid of the player playing
     *
     * @return the uuid of the player
     */
    UUID getPlayer();

    /**
     * tick the anime
     * if the anime is end, return true
     * @return true if end
     */
    boolean tick();

    /**
     * end the anime
     * @return true if successful
     */
    boolean endAnime();
}
