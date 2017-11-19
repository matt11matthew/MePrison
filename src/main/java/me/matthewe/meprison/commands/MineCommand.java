package me.matthewe.meprison.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.database.mine.MineDao;
import me.matthewe.meprison.lanaguage.LanguageConfig;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 11/19/2017.
 */
@CommandAlias("mine")
public class MineCommand extends BaseCommand {

    @Subcommand("create")
    public void onCreate(Player player, String name) {
        MineDao mineDao = MePrison.getMineDao();
        if (mineDao.isMine(name)) {
            player.sendMessage(LanguageConfig.getLanguage(player).mineAlreadyExists.replaceAll("%mine%", name));
            return;
        }
    }
}
