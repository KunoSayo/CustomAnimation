package com.github.euonmyoji.customanimation.api;

import com.github.euonmyoji.customanimation.api.data.IAnimeData;
import com.github.euonmyoji.customanimation.api.tasks.IAnimeTask;

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

    /**
     * end a player's anime
     * @param uuid the uuid of the player
     * @return true if successful
     */
    boolean endAnime(UUID uuid);

    /**
     * get the anime by anime id
     * @param id the id of the anime
     * @return {@link String} the id of the anime
     */
    IAnimeData getAnime(String id);

    /**
     * set the anime
     * @param data the data of the anime
     * @return the old anime data if present.
     */
    IAnimeData setAnime(IAnimeData data);

    /**
     * clear all the anime data (for reload?)
     */
    void clearData();
}
