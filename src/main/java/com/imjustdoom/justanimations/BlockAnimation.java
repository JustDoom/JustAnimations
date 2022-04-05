package com.imjustdoom.justanimations;

import com.imjustdoom.justanimations.util.BlockVector;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BlockAnimation {

    private World world;
    private Map<Integer, AnimationFrame> frames = new HashMap<>();
    private BukkitTask runnable;
    private boolean reverse, reverseSpeedUp, running = false;

    public BlockAnimation() {
    }

    public BlockAnimation(World world) {
        this.world = world;
    }

    public void addFrame(int frameNumber, AnimationFrame frame) {
        frames.put(frameNumber, frame);
    }

    // TODO: Add a method to remove a frame
    public void removeFrame(int frame) {
        frames.remove(frame);
        int last = -1;
        for(int i : frames.keySet()) {
            if(i - 1 != last) {

            }
            last = i;
        }
    }

    public boolean gotoFrame(int frame) {
        if(!this.runnable.isCancelled()) {
            this.runnable.cancel();
            this.running = false;
        }
        if(getFrames().get(frame) == null) return false;
        this.frame = frame;
        for(BlockVector loc : getFrames().get(frame).getBlockVectors()) {
            this.world.getBlockAt(loc.getX(), loc.getY(), loc.getZ()).setBlockData(loc.getBlockData());
        }
        return true;
    }

    private int frame = 0, timer = 0;
    public boolean goingReverse = false;
    public void play() {
        running = true;
        runnable = Bukkit.getScheduler().runTaskTimer(JustAnimations.INSTANCE, () -> {
            if(timer == (goingReverse ? frames.get(frame).getDelay() / 2 : frames.get(frame).getDelay())) {
                for(BlockVector loc : getFrames().get(frame).getBlockVectors()) {
                    world.getBlockAt(loc.getX(), loc.getY(), loc.getZ()).setBlockData(loc.getBlockData());
                }
                if(!reverse) {
                    frame = frame + 1 == frames.size() ? 0 : ++frame;
                } else {
                    if(goingReverse) {
                        if(frame - 1 == -1) {
                            goingReverse = false;
                            ++frame;
                        } else {
                            --frame;
                        }
                    } else {
                        if(frame + 1 == frames.size()) {
                            goingReverse = true;
                            --frame;
                        } else {
                            ++frame;
                        }
                    }
                }
                timer = 0;
            }
            timer++;
        }, 0L, 0L);
    }

    public void stop() {
        runnable.cancel();
        running = false;
    }
}
