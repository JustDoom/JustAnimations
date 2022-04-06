package com.imjustdoom.justanimations;

import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.command.AnimationsCommand;
import com.imjustdoom.justanimations.command.tabcomplete.AnimationsTabCompletion;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import com.imjustdoom.justanimations.listener.PlayerJoinListener;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class JustAnimations extends JavaPlugin {

    public static JustAnimations INSTANCE;

    private final Map<String, IAnimation> animations = new HashMap<>();

    public JustAnimations() {
        INSTANCE = this;
    }

    public static JustAnimations getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {

        int pluginId = 14842;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SingleLineChart("animations", animations::size));

        saveDefaultConfig();

        AnimationsConfig.load();

        getCommand("justanimations").setExecutor(new AnimationsCommand());
        getCommand("justanimations").setTabCompleter(new AnimationsTabCompletion());

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }
}