package com.github.euonmyoji.customanimation.api;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.util.annotation.NonnullByDefault;

/**
 * @author yinyangshi
 */
@NonnullByDefault
public class ReloadEvent implements Event {
    private final EventContext context = EventContext.builder().build();
    private final Cause cause = Cause.builder().build(context);
    private final CommandSource src;

    public ReloadEvent(CommandSource src) {
        this.src = src;
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    @Override
    public CommandSource getSource() {
        return src;
    }

    @Override
    public EventContext getContext() {
        return context;
    }
}
