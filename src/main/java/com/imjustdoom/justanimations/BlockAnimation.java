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
    private boolean reverse;

    public BlockAnimation() {
    }

    public BlockAnimation(World world) {
        this.world = world;
    }

    public void addFrame(AnimationFrame frame) {
        frames.put(frames.size(), frame);
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

    public void gotoFrame(int frame) {
        if(!runnable.isCancelled()) runnable.cancel();
        for(BlockVector loc : getFrames().get(frame).getBlockVectors()) {
            loc.getWorld().getBlockAt(loc.getX(), loc.getY(), loc.getZ()).setBlockData(loc.getBlockData());
        }
    }

    private int frame = 0, timer = 0;
    private boolean goingReverse = false;
    public void play() {
        runnable = Bukkit.getScheduler().runTaskTimer(JustAnimations.INSTANCE, () -> {
            System.out.println(frame);
            if(timer == getFrames().get(frame).getDelay()) {
                for(BlockVector loc : getFrames().get(frame).getBlockVectors()) {
                    loc.getWorld().getBlockAt(loc.getX(), loc.getY(), loc.getZ()).setBlockData(loc.getBlockData());
                }
                if(!reverse) {
                    frame = frame + 1 == frames.size() ? 0 : ++frame;
                } else {
                    //TODO: fix start and end frames repeating twice
                    if(goingReverse) {
                        if(frame - 1 == -1) {
                            goingReverse = false;
                            //++frame;
                        } else {
                            --frame;
                        }
                    } else {
                        if(frame + 1 == frames.size()) {
                            goingReverse = true;
                            //--frame;
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
    }
}
