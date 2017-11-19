package me.matthewe.meprison.player.collection;

import me.matthewe.meprison.player.OfflinePrisonPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface IOfflinePrisonPlayerCollection extends  IPrisonPlayerCollection<OfflinePrisonPlayer> {
    void addPlayer(OfflinePrisonPlayer prisonPlayer);

    void removePlayer(OfflinePrisonPlayer prisonPlayer);

    List<OfflinePrisonPlayer> getPrisonPlayerList();

    Map<UUID,  OfflinePrisonPlayer> getPrisonPlayerMap();
}
