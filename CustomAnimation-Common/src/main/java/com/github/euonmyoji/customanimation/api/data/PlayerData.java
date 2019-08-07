package com.github.euonmyoji.customanimation.api.data;

/**
 * the player data
 *
 * @author yinyangshi
 */
public interface PlayerData {
    /**
     * get the count of the anime this player have played
     *
     * @param id the id of the {@link IAnimeData}
     * @return the count
     * @throws Exception the exception getting the data
     */
    int getAnimePlayCount(String id) throws Exception;

    /**
     * set player's count have played
     *
     * @param id    the id of the {@link IAnimeData}
     * @param count the count of the anime play times
     * @throws Exception the exception getting the data
     */
    void setAnimePlayCount(String id, int count) throws Exception;
}
