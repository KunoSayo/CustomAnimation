package com.github.euonmyoji.customanimation.api;

import com.github.euonmyoji.customanimation.api.data.PlayerData;

import java.util.UUID;

/**
 * @author yinyangshi
 */
public interface PlayerDataManager {
    /**
     * get the player data
     * @param uuid the uuid of the player
     * @return {@link PlayerData} the player's data
     */
    PlayerData getPlayerData(UUID uuid);
}
