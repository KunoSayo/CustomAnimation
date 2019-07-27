package com.github.euonmyoji.customanimation.api;

import java.util.UUID;

/**
 * @author yinyangshi
 */
public interface AnimeManager {
    /**
     * whether a player is playing a anime
     * @param uuid the uuid of the player
     * @return true if the player is playing anime
     */
    IAnimeTask getPlaying(UUID uuid);

    /**
     * set the player anime task
     * @param uuid the uuid of the player
     * @param task the task of the anime
     * @return true if set successful, otherwise means already in this state
     */
    boolean setTask(UUID uuid, IAnimeTask task);

    /**
     * tick the anime task
     */
    void tick();
}
