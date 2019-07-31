package com.github.euonmyoji.customanimation.api.tasks;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

/**
 * @author yinyangshi
 */
public class TaskGroup implements IAnimeTask {
    private final int endTick;
    private final LinkedList<IAnimeTask> executing = new LinkedList<>();
    private final Queue<List<IAnimeTask>> keyTasks;
    private final int[] tick;
    private final UUID playerUUID;
    private int curTick = 1;
    private int curTask = 0;


    public TaskGroup(int endTick, LinkedList<List<IAnimeTask>> keyTasks, int[] tick, UUID playerUUID) {
        this.endTick = endTick;
        this.keyTasks = keyTasks;
        this.tick = tick;
        this.playerUUID = playerUUID;
        if (curTick > tick[curTask]) {
            submitNew();
        }
    }

    @Override
    public boolean isEnd() {
        return curTick > endTick;
    }

    @Override
    public UUID getPlayer() {
        return playerUUID;
    }

    @Override
    public boolean tick() {
        if (curTick++ > tick[curTask]) {
            submitNew();
        }
        executing.removeIf(IAnimeTask::tick);
        return isEnd();
    }

    @Override
    public boolean endAnime() {
        if (isEnd()) {
            return false;
        }
        for (IAnimeTask iAnimeTask : executing) {
            iAnimeTask.endAnime();
        }
        curTick = endTick + 1;
        return true;
    }

    private void submitNew() {
        if (!keyTasks.isEmpty()) {
            executing.addAll(keyTasks.poll());
        }
        ++curTask;
    }
}
