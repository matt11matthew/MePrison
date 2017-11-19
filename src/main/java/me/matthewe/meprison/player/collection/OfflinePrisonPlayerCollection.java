package me.matthewe.meprison.player.collection;

import me.matthewe.meprison.player.OfflinePrisonPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class OfflinePrisonPlayerCollection implements IOfflinePrisonPlayerCollection {
    private Map<UUID, OfflinePrisonPlayer> prisonPlayerMap;

    public OfflinePrisonPlayerCollection() {
        this.prisonPlayerMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addPlayer(OfflinePrisonPlayer prisonPlayer) {
        prisonPlayerMap.put(prisonPlayer.getUuid(), prisonPlayer);
    }

    @Override
    public void removePlayer(OfflinePrisonPlayer prisonPlayer) {
        prisonPlayerMap.remove(prisonPlayer.getUuid());
    }

    @Override
    public List<OfflinePrisonPlayer> getPrisonPlayerList() {
        return new ArrayList<>(prisonPlayerMap.values());
    }

    @Override
    public Map<UUID, OfflinePrisonPlayer> getPrisonPlayerMap() {
        return prisonPlayerMap;
    }


}
