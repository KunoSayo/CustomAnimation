package com.github.euonmyoji.customanimation.api.data;

import com.github.euonmyoji.customanimation.api.tasks.IAnimeTask;

import java.util.UUID;

/**
 * @author yinyangshi
 */
public interface IAnimeData {
    /**
     * create the task from the data
     * @param playerUUID the player of the uuid to get the task
     * @return the new task
     */
    IAnimeTask getTask(UUID playerUUID);
}
