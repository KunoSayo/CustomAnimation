package com.github.euonmyoji.customanimation.api.tasks;

import java.util.UUID;

/**
 * @author yinyangshi
 */
public class LookTrackTask implements IAnimeTask {
    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public UUID getPlayer() {
        return null;
    }

    @Override
    public boolean tick() {
        return false;
    }

    @Override
    public boolean endAnime() {
        return false;
    }
}
