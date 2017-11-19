package me.matthewe.meprison.listeners.player;

import me.matthewe.meprison.MePrison;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class ChatListener implements Listener {
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        MePrison.getPrisonPlayerDao().requestPrisonPlayer(player, prisonPlayer -> {
            event.setFormat(prisonPlayer.getRank().formatMessage(player.getName(), ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', event.getMessage()))));
        });
    }
}







