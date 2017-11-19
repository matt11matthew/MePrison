package me.matthewe.meprison.player.collection;

import me.matthewe.meprison.player.OnlinePrisonPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface IOnlinePrisonPlayerCollection extends IPrisonPlayerCollection<OnlinePrisonPlayer> {
    void addPlayer(OnlinePrisonPlayer prisonPlayer);

    void removePlayer(OnlinePrisonPlayer prisonPlayer);

    List<OnlinePrisonPlayer> getPrisonPlayerList();

    Map<UUID,  OnlinePrisonPlayer> getPrisonPlayerMap();
}
