package com.github.euonmyoji.customanimation.manager;

import com.github.euonmyoji.customanimation.api.AnimeManager;
import com.github.euonmyoji.customanimation.api.tasks.IAnimeTask;
import com.github.euonmyoji.customanimation.configuration.PluginConfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author yinyangshi
 */
public class AnimeManagerImpl implements AnimeManager {
    private final HashMap<UUID, IAnimeTask> tasks = new HashMap<>();

    @Override
    public IAnimeTask getPlaying(UUID uuid) {
        return tasks.get(uuid);
    }

    @Override
    public boolean setTask(UUID uuid, IAnimeTask task) {
        if (tasks.containsKey(uuid)) {
            return false;
        }
        tasks.put(uuid, task);
        return true;
    }

    @Override
    public void tick() {
        Collection<IAnimeTask> c = tasks.values();
        if (c.size() > PluginConfig.parallelGoal) {
            c.parallelStream().filter(IAnimeTask::tick).forEach(task -> tasks.remove(task.getPlayer()));
        } else {
            c.stream().filter(IAnimeTask::tick).forEach(task -> tasks.remove(task.getPlayer()));
        }
    }

    @Override
    public boolean endAnime(UUID uuid) {
        IAnimeTask task = tasks.get(uuid);
        return task != null && task.endAnime() && tasks.remove(uuid, task);
    }
}
