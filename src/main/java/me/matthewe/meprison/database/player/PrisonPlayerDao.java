package me.matthewe.meprison.database.player;

import me.matthewe.meprison.player.OfflinePrisonPlayer;
import me.matthewe.meprison.player.OnlinePrisonPlayer;
import me.matthewe.meprison.player.PrisonPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface PrisonPlayerDao {
    List<OnlinePrisonPlayer> getPrisonPlayerList();
    void updatePrisonPlayer(OnlinePrisonPlayer prisonPlayer, Runnable success, Runnable failure);
    void updatePrisonPlayer(OnlinePrisonPlayer prisonPlayer, Runnable success);
    void updatePrisonPlayer(OnlinePrisonPlayer prisonPlayer);
    void deletePrisonPlayer(OnlinePrisonPlayer prisonPlayer);
    int getRowCount();
    void requestPrisonPlayer(Player player, Consumer<OnlinePrisonPlayer> success);
    void requestOfflinePrisonPlayer(UUID uuid, Consumer<OfflinePrisonPlayer> success, Runnable failure);
    void requestOfflinePrisonPlayer(UUID uuid, Consumer<OfflinePrisonPlayer> success);
    void requestOfflinePrisonPlayer(String name, Consumer<OfflinePrisonPlayer> success);
    void requestOfflinePrisonPlayer(String name, Consumer<OfflinePrisonPlayer> success, Runnable failure);
    void requestGenericPrisonPlayer(UUID uuid, Consumer<PrisonPlayer> success);
}
