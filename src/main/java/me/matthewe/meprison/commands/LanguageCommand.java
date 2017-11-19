package me.matthewe.meprison.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.lanaguage.LanguageConfig;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 11/19/2017.
 */
@CommandAlias("language|lang")
public class LanguageCommand extends BaseCommand {

    @CommandAlias("language|lang")
    public void onLanguage(Player player) {
        MePrison.getPrisonPlayerDao().requestPrisonPlayer(player, onlinePrisonPlayer -> {
            player.sendMessage(LanguageConfig.getLanguage(player).yourLanguage.replaceAll("%language%", onlinePrisonPlayer.getLanguage()));
        });
    }
}
