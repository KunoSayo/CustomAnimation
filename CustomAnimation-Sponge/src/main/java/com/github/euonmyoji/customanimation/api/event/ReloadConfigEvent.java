package com.github.euonmyoji.customanimation.api.event;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.util.annotation.NonnullByDefault;

/**
 * @author yinyangshi
 */
@NonnullByDefault
public class ReloadConfigEvent implements Event, IReloadConfigEvent {
    private final MessageReceiver receiver;
    private final Cause cause;
    private final EventContext context;

    public ReloadConfigEvent(MessageReceiver receiver) {
        this.receiver = receiver;
        context = EventContext.builder().build();
        cause = Cause.builder().append(receiver).build(context);
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    @Override
    public Object getSource() {
        return receiver;
    }

    @Override
    public EventContext getContext() {
        return context;
    }
}
