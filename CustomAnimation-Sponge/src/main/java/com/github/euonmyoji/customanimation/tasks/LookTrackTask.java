package com.github.euonmyoji.customanimation.tasks;

import com.github.euonmyoji.customanimation.api.IAnimeTask;

import java.util.UUID;

/**
 * @author yinyangshi
 */
public class LookTrackTask implements IAnimeTask {
    @Override
    public boolean end() {
        return false;
    }

    @Override
    public UUID getPlayer() {
        return null;
    }

    @Override
    public void run() {

    }
}
