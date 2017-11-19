package me.matthewe.meprison.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.chat.rank.Rank;
import me.matthewe.meprison.database.player.PrisonPlayerDao;
import me.matthewe.meprison.lanaguage.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Matthew E on 11/18/2017.
 */
@CommandAlias("rank")
public class RankCommand extends BaseCommand {

    @CommandAlias("rank")
    public void onRank(Player player) {
        MePrison.getPrisonPlayerDao().requestPrisonPlayer(player, prisonPlayer -> player.sendMessage(ChatColor.GREEN + "Rank: " + ChatColor.translateAlternateColorCodes('&', prisonPlayer.getRank().getPrefix())));
    }

    @Subcommand("set")
    public void onRankSet(CommandSender player, String name, String rankName) {
        if (!Arrays.stream(Rank.values()).map(rank1 -> rank1.toString().toLowerCase()).collect(Collectors.toList()).contains(rankName.toLowerCase())) {
            player.sendMessage(LanguageConfig.getLanguage().rankDoesntExist.replaceAll("%rankName%", rankName));
            return;
        }
        PrisonPlayerDao prisonPlayerDao = MePrison.getPrisonPlayerDao();
        Rank rank = Rank.valueOf(rankName.toUpperCase());
        Player player1 = Bukkit.getPlayer(name);
        if (player1 != null && (player1.isOnline())) {
            prisonPlayerDao.requestPrisonPlayer(player1, onlinePrisonPlayer -> {
                onlinePrisonPlayer.setRank(rank);
                player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GRAY + onlinePrisonPlayer.getName() + "'s " + ChatColor.GREEN + " rank to " + ChatColor.translateAlternateColorCodes('&', rank.getPrefix()) + ChatColor.GRAY + ".");
            });
        } else {
            prisonPlayerDao.requestOfflinePrisonPlayer(name, offlinePrisonPlayer -> {
                offlinePrisonPlayer.setRank(rank);
                player.sendMessage(ChatColor.GREEN + "Set " + ChatColor.GRAY + offlinePrisonPlayer.getName() + "'s " + ChatColor.GREEN + " rank to " + ChatColor.translateAlternateColorCodes('&', rank.getPrefix()) + ChatColor.GRAY + ".");
            });
        }
    }
}
