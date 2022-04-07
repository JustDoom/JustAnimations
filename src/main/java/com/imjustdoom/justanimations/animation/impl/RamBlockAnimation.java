package com.imjustdoom.justanimations.animation.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.api.events.AnimationEndEvent;
import com.imjustdoom.justanimations.api.events.AnimationFrameChangeEvent;
import com.imjustdoom.justanimations.api.events.AnimationStartEvent;
import com.imjustdoom.justanimations.api.util.BlockVector;
import com.imjustdoom.justanimations.storage.DataStore;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RamBlockAnimation implements IAnimation {

    private DataStore dataStore;
    private String name;
    private World world;
    private Map<Integer, AnimationFrame> frames = new HashMap<>();
    private BukkitTask runnable;
    private boolean reverse, reverseSpeedUp, running = false;
    private int frameCount;

    public RamBlockAnimation() {
    }

    public RamBlockAnimation(World world, DataStore dataStore, String name) {
        this.dataStore = dataStore;
        this.world = world;
        this.name = name;
    }

    public void addFrame(String frameNumber, AnimationFrame frame) {
        frames.put(Integer.valueOf(frameNumber), frame);
    }

    // TODO: Add a method to remove a frame
    public void removeFrame(int frame) {
        frames.remove(frame);
        int last = -1;
        for (int i : frames.keySet()) {
            if (i - 1 != last) {

            }
            last = i;
        }
    }

    public boolean gotoFrame(int frame) {
        if (!this.runnable.isCancelled()) {
            this.runnable.cancel();
            this.running = false;
        }

        frames.remove(this.frame);

        File animationFile = new File(dataStore.getDataFolder() + frame + ".yml");
        if (!animationFile.exists()) return false;

        this.frame = frame;

        AnimationFrame animationFrame = frames.get(frame);

        for (BlockVector loc : getFrames().get(frame).getBlockVectors().keySet()) {
            BlockData blockData = getFrames().get(this.frame).getBlockVectors().get(loc);
            Block block = this.world.getBlockAt(loc.getX(), loc.getY(), loc.getZ());
            if (block.getBlockData() == blockData) continue;
            block.setBlockData(blockData);
        }

        AnimationFrameChangeEvent animationFrameChangeEvent = new AnimationFrameChangeEvent(this);
        Bukkit.getPluginManager().callEvent(animationFrameChangeEvent);

        return true;
    }

    private int frame = 0, timer = 0;
    public boolean goingReverse = false;

    public void play() {
        running = true;
        runnable = Bukkit.getScheduler().runTaskTimer(JustAnimations.INSTANCE, () -> {
            if (Bukkit.getOnlinePlayers().size() == 0 || frames.size() == 0) {
                stop();
                return;
            }
            if (timer == (goingReverse ? frames.get(frame).getDelay() / 2 : frames.get(frame).getDelay())) {

                for (BlockVector loc : getFrames().get(frame).getBlockVectors().keySet()) {
                    BlockData blockData = getFrames().get(frame).getBlockVectors().get(loc);
                    Block block = this.world.getBlockAt(loc.getX(), loc.getY(), loc.getZ());
                    if (block.getBlockData() == blockData) continue;
                    block.setBlockData(blockData);
                }

                frames.remove(frame);

                if (!reverse) {
                    frame = frame + 1 == frameCount ? 0 : frame + 1;
                } else {
                    if (goingReverse) {
                        if (frame - 1 == -1) {
                            goingReverse = false;
                            frame++;
                        } else frame--;
                    } else {
                        if (frame + 1 == -1) {
                            goingReverse = true;
                            frame--;
                        } else frame++;
                    }
                }

                AnimationFrameChangeEvent animationFrameChangeEvent = new AnimationFrameChangeEvent(this);
                Bukkit.getPluginManager().callEvent(animationFrameChangeEvent);

                timer = 0;
            }
            timer++;
        }, 0L, 0L);
        AnimationStartEvent animationStartEvent = new AnimationStartEvent(this);
        Bukkit.getPluginManager().callEvent(animationStartEvent);
    }

    public void stop() {
        runnable.cancel();
        running = false;
        AnimationEndEvent animationEndEvent = new AnimationEndEvent(this);
        Bukkit.getPluginManager().callEvent(animationEndEvent);
    }
}
