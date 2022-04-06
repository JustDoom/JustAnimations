package com.imjustdoom.justanimations.animation;

import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import org.bukkit.World;

import java.util.Map;

public interface IAnimation {

    void play();

    void stop();

    void addFrame(int frameNumber, AnimationFrame frame);

    void removeFrame(int frame);

    boolean gotoFrame(int frame);

    boolean isReverse();

    void setReverse(boolean reverse);

    void setGoingReverse(boolean reverse);

    World getWorld();

    void setWorld(World world);

    Map<Integer, AnimationFrame> getFrames();

    int getFrame();

    boolean isRunning();
}
