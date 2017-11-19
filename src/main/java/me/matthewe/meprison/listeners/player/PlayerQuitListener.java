package me.matthewe.meprison.listeners.player;

import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.lanaguage.LanguageConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        MePrison.getPrisonPlayerDao().requestPrisonPlayer(event.getPlayer(), prisonPlayer -> {
            prisonPlayer.setWarping(false);
            prisonPlayer.setWarpCooldown(null);
            MePrison.getPrisonPlayerDao().updatePrisonPlayer(prisonPlayer, null, null);
            LanguageConfig.playerLanguageMap.remove(prisonPlayer.getUuid());
        });
    }
}
