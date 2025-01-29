package com.imjustdoom.justanimations;

import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.command.CommandManager;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import com.imjustdoom.justanimations.listener.PlayerListener;
import com.imjustdoom.justanimations.metrics.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JustAnimations extends JavaPlugin {

    public static JustAnimations INSTANCE;

    private final Map<String, IAnimation> animations = new HashMap<>();
    private final List<String> converting = new ArrayList<>();

    private final String animationDataFolder = getDataFolder() + "/data/";

    public JustAnimations() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {

        int pluginId = 14842;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("animations", animations::size));

        saveDefaultConfig();

        AnimationsConfig.load();

        getCommand("justanimations").setExecutor(new CommandManager());
        getCommand("justanimations").setTabCompleter(new CommandManager());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public Map<String, IAnimation> getAnimations() {
        return this.animations;
    }

    public List<String> getConverting() {
        return this.converting;
    }

    public String getAnimationDataFolder() {
        return this.animationDataFolder;
    }
}