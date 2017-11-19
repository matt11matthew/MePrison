package me.matthewe.meprison.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import me.matthewe.meprison.MePrison;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

/**
 * Created by Matthew E on 11/18/2017.
 */
@CommandAlias("balance|bal|b|money|currency|bank|be")
public class BalanceCommand extends BaseCommand {

    @CommandAlias("balance|bal|b|money|currency|bank|be")
    public void onBalance(Player player) {
        MePrison.getPrisonPlayerDao().requestPrisonPlayer(player, prisonPlayer -> player.sendMessage(ChatColor.GREEN + "Balance: " + ChatColor.GRAY + "$" + new DecimalFormat("#,###.##").format(prisonPlayer.getBalance())));
    }

    @CommandAlias("balance|bal|b|money|currency|bank|be")
    public void onBalanceOther(Player player, String name) {
        MePrison.getPrisonPlayerDao().requestOfflinePrisonPlayer(name, prisonPlayer -> player.sendMessage(ChatColor.GREEN + "Balance: " + ChatColor.GRAY + "$" + new DecimalFormat("#,###.##").format(prisonPlayer.getBalance())), () -> player.sendMessage(ChatColor.RED + name + " has never joined the server before."));
    }
}
