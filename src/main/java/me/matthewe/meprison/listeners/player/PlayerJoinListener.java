package me.matthewe.meprison.listeners.player;

import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.lanaguage.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        MePrison.getPrisonPlayerDao().requestPrisonPlayer(event.getPlayer(), prisonPlayer -> {
            if (prisonPlayer.isFirstSession()) {
                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Welcome " + ChatColor.GRAY +  prisonPlayer.getName() + ChatColor.GREEN + " to MePrison " + ChatColor.GRAY +  "#" + MePrison.getPrisonPlayerDao().getRowCount());
            }
            LanguageConfig.playerLanguageMap.put(prisonPlayer.getUuid(), prisonPlayer.getLanguage());
        });
    }
}







