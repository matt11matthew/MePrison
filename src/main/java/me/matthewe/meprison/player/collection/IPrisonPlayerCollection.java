package me.matthewe.meprison.player.collection;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface IPrisonPlayerCollection<V> {
    void addPlayer(V prisonPlayer);

    void removePlayer(V prisonPlayer);

    List<V> getPrisonPlayerList();

    Map<UUID, V> getPrisonPlayerMap();
}
