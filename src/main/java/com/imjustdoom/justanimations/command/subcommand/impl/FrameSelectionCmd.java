package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import com.imjustdoom.justanimations.storage.DataStore;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class FrameSelectionCmd implements SubCommand {

    public String getName() {
        return "frameselection";
    }

    public String getDescription() {
        return "Makes your WorldEdit selection the selection of the frame";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (org.bukkit.entity.Player) sender)) {
            return;
        }

        if(args.length == 3) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GO_TO_FRAME_NOT_EXISTS,
                    args[1],
                    "invalid"));
            return;
        }
        if (!JustAnimations.INSTANCE.getAnimations().get(args[1]).gotoFrame(Integer.parseInt(args[3]))) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GO_TO_FRAME_NOT_EXISTS,
                    args[1],
                    args[3]));
            return;
        }

        int z1, z2, x1, x2, y1 , y2;
        String x2Section, y2Section, z2Section, x1Section, y1Section, z1Section;

        DataStore animation = JustAnimations.INSTANCE.getAnimations().get(args[1]).getDataStore();

        Set<String> x1SectionSet = animation.getFrame(args[3]).getConfigurationSection("blocks").getKeys(false);
        x1Section = new ArrayList<>(x1SectionSet).get(x1SectionSet.size() - 1);
        x1 = Integer.parseInt(x1Section);

        Set<String> y1SectionSet = animation.getFrame(args[3]).getConfigurationSection("blocks").getConfigurationSection(x1Section).getKeys(false);
        y1Section = new ArrayList<>(y1SectionSet).get(y1SectionSet.size() - 1);
        y1 = Integer.parseInt(y1Section);

        Set<String> z1SectionSet = animation.getFrame(args[3]).getConfigurationSection("blocks").getConfigurationSection(x1Section).getConfigurationSection(y1Section).getKeys(false);
        z1Section = new ArrayList<>(z1SectionSet).get(z1SectionSet.size() - 1);
        z1 = Integer.parseInt(z1Section);
        

        Set<String> x2SectionSet = animation.getFrame(args[3]).getConfigurationSection("blocks").getKeys(false);
        x2Section = new ArrayList<>(x2SectionSet).get(0);
        x2 = Integer.parseInt(x2Section);

        Set<String> y2SectionSet = animation.getFrame(args[3]).getConfigurationSection("blocks").getConfigurationSection(x2Section).getKeys(false);
        y2Section = new ArrayList<>(y2SectionSet).get(0);
        y2 = Integer.parseInt(y2Section);

        Set<String> z2SectionSet = animation.getFrame(args[3]).getConfigurationSection("blocks").getConfigurationSection(x2Section).getConfigurationSection(y2Section).getKeys(false);
        z2Section = new ArrayList<>(z2SectionSet).get(0);
        z2 = Integer.parseInt(z2Section);

        com.sk89q.worldedit.entity.Player wePlayer = BukkitAdapter.adapt((Player) sender);
        WorldEdit.getInstance().getSessionManager().get(wePlayer).setRegionSelector(wePlayer.getWorld(),
                new CuboidRegionSelector(wePlayer.getWorld(), BlockVector3.at(x1, y1, z1), BlockVector3.at(x2, y2, z2)));

        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.FRAME_SELECTION_SUCCESS,
                args[1],
                args[3]));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.frameselection", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }
        if (JustAnimations.INSTANCE.getAnimations().get(args[1]) == null) return Collections.emptyList();
        List<String> frames = new ArrayList<>();
        int i = 0;
        while(i <= JustAnimations.INSTANCE.getAnimations().get(args[1]).getFrameCount() - 1){
            frames.add(String.valueOf(i));
            i++;
        }
        return frames;
    }
}