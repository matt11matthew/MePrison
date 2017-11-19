package me.matthewe.meprison.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.database.WarpDao;
import me.matthewe.meprison.player.WarpCooldownImpl;
import me.matthewe.meprison.warp.Warp;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


/**
 * Created by Matthew E on 11/18/2017.
 */
@CommandAlias("warp|warps")
public class WarpCommand extends BaseCommand {
    @CommandAlias("warp|warps")
    public void onWarp(Player player) {
        WarpDao warpDao = MePrison.getWarpDao();
        player.sendMessage(ChatColor.GREEN + "Warps: " + ChatColor.GRAY +  warpDao.getRowCount());
        String warpString = "";
        TextComponent textComponent = new TextComponent("");
        int index = warpDao.getWarpList().size();
        for (Warp warp : warpDao.getWarpList()) {
            TextComponent textComponent1 = new TextComponent(ChatColor.GREEN + warp.getName());
            textComponent1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(ChatColor.GREEN + "Click to warp\n"),
                    new TextComponent(ChatColor.GRAY + "---------------------\n"),
                    new TextComponent(ChatColor.GREEN + "Name: " + ChatColor.GRAY + warp.getName() + "\n"),
                    new TextComponent(ChatColor.GREEN + "Time: " + ChatColor.GRAY + warp.getWarpTime() + "s\n"),
                    new TextComponent(ChatColor.GRAY + "---------------------"),
            }));

                    textComponent1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warp.getName()));
            textComponent.addExtra(textComponent1);
            index--;
            if (index > 0) {
                textComponent.addExtra(ChatColor.GRAY + ", ");
            }
        }
        player.spigot().sendMessage(textComponent);
    }

    @CommandPermission("warp.delete")
    @Subcommand("delete")
    public void onWarpDelete(Player player, String name) {
        WarpDao warpDao = MePrison.getWarpDao();
        if (!warpDao.isWarp(name)) {
            player.sendMessage(ChatColor.RED + "The warp " + name + " doesn't exist.");
            return;
        }
        Warp warp = warpDao.getWarp(name);
        warpDao.removeWarp(warp);
        player.sendMessage(ChatColor.GREEN + "Deleted warp " + ChatColor.GRAY + warp.getName() + ChatColor.GREEN + ".");
    }

    @CommandPermission("warp.create")
    @Subcommand("create")
    public void onWarpCreate(Player player, String name, Integer time) {
        WarpDao warpDao = MePrison.getWarpDao();
        if (warpDao.isWarp(name)) {
            player.sendMessage(ChatColor.RED + "The warp " + name + " already exist.");
            return;
        }
        if (time < 0) {
            time = 0;
        }
        Warp warp = new Warp(name, player.getLocation(), time);
        warpDao.createWarp(warp);
        player.sendMessage(ChatColor.GREEN + "Created warp " + ChatColor.GRAY + warp.getName() + ChatColor.GREEN +" with delay of " + ChatColor.GRAY + warp.getWarpTime() + "s" + ChatColor.GREEN + ".");
    }

    @CommandAlias("warp")
    public void onWarp(Player player, String name) {
        WarpDao warpDao = MePrison.getWarpDao();
        if (!warpDao.isWarp(name)) {
            player.sendMessage(ChatColor.RED + "The warp " + name + " doesn't exist.");
        } else {
            Warp warp = warpDao.getWarp(name);
            MePrison.getPrisonPlayerDao().requestPrisonPlayer(player, onlinePrisonPlayer -> {
                if (onlinePrisonPlayer.isWarping()) {
                    player.sendMessage(ChatColor.RED +"You're already warping.");
                    return;
                }
                if (warp.getWarpTime() < 1) {
                    player.teleport(warp.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Warped to " + ChatColor.GRAY + warp.getName() + ChatColor.GREEN + ".");
                    onlinePrisonPlayer.setWarping(false);
                    onlinePrisonPlayer.setWarpCooldown(null);
                } else {
                    onlinePrisonPlayer.setWarping(true);
                    onlinePrisonPlayer.setWarpCooldown(new WarpCooldownImpl(warp));
                    player.sendMessage(ChatColor.GREEN + "Warping in " + ChatColor.GRAY + warp.getWarpTime() + "s" + ChatColor.GREEN + ".");
                }
            });
        }
    }
}
