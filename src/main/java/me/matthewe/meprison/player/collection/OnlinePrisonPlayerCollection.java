package me.matthewe.meprison.player.collection;

import me.matthewe.meprison.player.OnlinePrisonPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class OnlinePrisonPlayerCollection implements IOnlinePrisonPlayerCollection {
    private Map<UUID, OnlinePrisonPlayer> prisonPlayerMap;

    public OnlinePrisonPlayerCollection() {
        this.prisonPlayerMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addPlayer(OnlinePrisonPlayer prisonPlayer) {
        prisonPlayerMap.put(prisonPlayer.getUuid(), prisonPlayer);
    }

    @Override
    public void removePlayer(OnlinePrisonPlayer prisonPlayer) {
        prisonPlayerMap.remove(prisonPlayer.getUuid());
    }

    @Override
    public List<OnlinePrisonPlayer> getPrisonPlayerList() {
        return new ArrayList<>(prisonPlayerMap.values());
    }

    @Override
    public Map<UUID, OnlinePrisonPlayer> getPrisonPlayerMap() {
        return prisonPlayerMap;
    }


}
