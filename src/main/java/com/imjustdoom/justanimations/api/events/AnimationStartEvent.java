package com.imjustdoom.justanimations.api.events;

import com.imjustdoom.justanimations.animation.IAnimation;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AnimationStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final IAnimation animation;

    public AnimationStartEvent(IAnimation animation) {
        this.animation = animation;
    }

    public IAnimation getAnimation() {
        return this.animation;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
