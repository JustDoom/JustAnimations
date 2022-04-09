package com.imjustdoom.justanimations.animation;

import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.storage.DataStore;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public interface IAnimation {

    void play();

    void stop();

    void addFrame(String frameNumber, AnimationFrame frame);

    void removeFrame(int frame);

    boolean gotoFrame(int frame);

    boolean isReverse();

    void setReverse(boolean reverse);

    void setGoingReverse(boolean reverse);

    World getWorld();

    void setWorld(World world);

    Map<Integer, AnimationFrame> getFrames();

    int getFrame();

    int getFrameCount();

    void setFrameCount(int frameCount);

    boolean isRunning();

    String getName();

    void setName(String name);

    DataStore getDataStore();

    void setDataStore(DataStore dataStore);

    BukkitTask getRunnable();

    boolean getSaveToRam();

    void setSaveToRam(boolean saveToRam);
}
