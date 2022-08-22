package com.imjustdoom.justanimations.listener;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if(Bukkit.getOnlinePlayers().size() == 1) {
            for(IAnimation animation : JustAnimations.INSTANCE.getAnimations().values()) {
                animation.play(false);
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        if(Bukkit.getOnlinePlayers().size() == 0) {
            for(IAnimation animation : JustAnimations.INSTANCE.getAnimations().values()) {
                animation.stop();
            }
        }
    }
}