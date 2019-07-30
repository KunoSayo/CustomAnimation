package com.github.euonmyoji.customanimation.api.tasks;

/**
 * @author yinyangshi
 */
public abstract class AbstractLastTask implements IAnimeTask {
    /**
     * 该任务总tick时长
     */
    protected final int tick;
    /**
     * 当前任务tick进度
     */
    protected int cur = 1;

    public AbstractLastTask(int tick) {
        this.tick = tick;
    }

    @Override
    public boolean isEnd() {
        return cur > tick;
    }

    @Override
    public boolean endAnime() {
        if (isEnd()) {
            return false;
        }
        cur = Integer.MAX_VALUE;
        return true;
    }

    public int getTick() {
        return this.tick;
    }

    public int getCur() {
        return this.cur;
    }
}
