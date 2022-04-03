package com.imjustdoom.justanimations;

import com.imjustdoom.justanimations.command.AnimeCommand;
import com.imjustdoom.justanimations.command.tabcomplete.AnimeTabCompletion;
import com.imjustdoom.justanimations.config.AnimeConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class JustAnimations extends JavaPlugin {

    public static JustAnimations INSTANCE;

    private final Map<String, BlockAnimation> animations = new HashMap<>();

    public JustAnimations() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();

        AnimeConfig.load();

        this.getCommand("justanimations").setExecutor(new AnimeCommand());
        this.getCommand("justanimations").setTabCompleter(new AnimeTabCompletion());
    }
}