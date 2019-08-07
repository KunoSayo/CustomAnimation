package com.github.euonmyoji.customanimation.api.event;

import com.github.euonmyoji.customanimation.CustomAnimation;
import com.github.euonmyoji.customanimation.api.data.IAnimeData;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.text.channel.MessageReceiver;

import javax.annotation.Nonnull;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author yinyangshi
 */
public class LoadAnimeDataEvent implements Event, Cancellable, ILoadTaskDataEvent {
    private final Cause cause;
    private final CommentedConfigurationNode node;
    private final MessageReceiver receiver;
    private boolean canceled = false;
    private IAnimeData cur;
    private Queue<Runnable> runnables = new ConcurrentLinkedQueue<>();

    public LoadAnimeDataEvent(@Nonnull MessageReceiver receiver, CommentedConfigurationNode node) {
        this.node = node.copy();
        this.receiver = receiver;
        cause = Cause.builder().append(receiver).append(node).build(EventContext.builder().build());
    }

    @Override
    @Nonnull
    public Cause getCause() {
        return cause;
    }

    @Override
    @Nonnull
    public MessageReceiver getSource() {
        return receiver;
    }

    public CommentedConfigurationNode getNode() {
        return node;
    }


    @Override
    public IAnimeData getCurrent() {
        return cur;
    }

    @Override
    public void setCurrent(IAnimeData task) {
        cur = task;
    }

    @Override
    public void onFailure(Runnable r) {
        runnables.add(r);
    }

    @Override
    public void failedAndClear() {
        Runnable r;
        while (!runnables.isEmpty()) {
            try {
                while ((r = runnables.poll()) != null) {
                    r.run();
                }
            } catch (Throwable t) {
                CustomAnimation.logger.warn("failed load task and failed to load runnable", t);
            }
        }
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }
}
