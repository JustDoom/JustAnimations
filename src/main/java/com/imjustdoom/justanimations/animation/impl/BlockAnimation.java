package com.imjustdoom.justanimations.animation.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.api.events.AnimationEndEvent;
import com.imjustdoom.justanimations.api.events.AnimationFrameChangeEvent;
import com.imjustdoom.justanimations.api.events.AnimationStartEvent;
import com.imjustdoom.justanimations.api.util.AnimationUtil;
import com.imjustdoom.justanimations.api.util.BlockVector;
import com.imjustdoom.justanimations.storage.DataStore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BlockAnimation implements IAnimation {

    private DataStore dataStore;
    private String name;
    private World world;
    private Map<Integer, AnimationFrame> frames = new HashMap<>();
    private BukkitTask runnable;
    private boolean reverse, reverseSpeedUp, saveToRam, randomFrame, running = false, goingReverse = false;
    private int frameCount, frame = 0, timer = 0;
    private boolean inactive = false;

    public BlockAnimation() {
    }

    public BlockAnimation(World world, DataStore dataStore, String name) {
        this.dataStore = dataStore;
        this.world = world;
        this.name = name;
    }

    public void addFrame(String frameNumber, AnimationFrame frame, ConfigurationSection section) {
        getDataStore().saveFrame(name, section, frame.getDelay(), String.valueOf(getFrameCount()));
        frameCount++;
        if(saveToRam || frames.size() == 0) frames.put(Integer.valueOf(frameNumber), frame);
    }

    public void editFrame(String frameNumber, AnimationFrame frame, ConfigurationSection section) {
        getDataStore().saveFrame(name, section, frame.getDelay(), frameNumber);
        if(saveToRam || frames.size() == 0) frames.put(Integer.valueOf(frameNumber), frame);
    }

    public void addFrame(String frameNumber, AnimationFrame frame) {
        if(saveToRam || frames.size() == 0) frames.put(Integer.valueOf(frameNumber), frame);
    }

    public boolean gotoFrame(int frame) {
        stop();

        if(!saveToRam) frames.remove(this.frame);

        this.frame = frame;

        if(!saveToRam) {
            AnimationFrame animationFrame = AnimationUtil.getFrame(this, String.valueOf(frame));
            if(animationFrame == null) return false;
            frames.put(frame, animationFrame);
        }

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

    public void play(boolean playOnce) {
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

                if(!saveToRam) frames.remove(frame);

                // Just don't touch...
                if(randomFrame) {
                    frame = (int) (Math.random() * frameCount);
                } else if (!reverse) {
                    if(frame + 1 == frameCount) {
                        if(playOnce) stop();
                        frame = 0;
                    } else {
                        frame++;
                    }
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

                if(!saveToRam) frames.put(frame, AnimationUtil.getFrame(this, String.valueOf(frame)));

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
        if(this.runnable == null || !this.running) return;
        runnable.cancel();
        running = false;
        AnimationEndEvent animationEndEvent = new AnimationEndEvent(this);
        Bukkit.getPluginManager().callEvent(animationEndEvent);
    }

    public void reload() {
        boolean running = this.running;
        stop();
        this.frames.clear();
        this.timer = this.frame = 0;
        this.goingReverse = false;
        this.running = false;
        this.setFrameCount(dataStore.getFrameCount());
        AnimationUtil.getFrames(this, new File(this.dataStore.getDataFolder()));
        if(running) play(false);
    }

    @Override
    public boolean isReverse() {
        return this.reverse;
    }

    @Override
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public void setGoingReverse(boolean goingReverse) {
        this.goingReverse = goingReverse;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public Map<Integer, AnimationFrame> getFrames() {
        return this.frames;
    }

    @Override
    public int getFrame() {
        return this.frame;
    }

    @Override
    public int getFrameCount() {
        return this.frameCount;
    }

    @Override
    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.dataStore.setName(name);
    }

    @Override
    public DataStore getDataStore() {
        return this.dataStore;
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public BukkitTask getRunnable() {
        return this.runnable;
    }

    @Override
    public boolean isSaveToRam() {
        return this.saveToRam;
    }

    @Override
    public void setSaveToRam(boolean saveToRam) {
        this.saveToRam = saveToRam;
    }

    @Override
    public boolean isRandomFrame() {
        return this.randomFrame;
    }

    @Override
    public void setRandomFrame(boolean randomFrame) {
        this.randomFrame = randomFrame;
    }

    @Override
    public boolean isInactive() {
        return this.inactive;
    }

    @Override
    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }
}
