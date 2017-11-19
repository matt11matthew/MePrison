package me.matthewe.meprison.mine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class MineCollection implements IMineCollection {
    private Map<String, Mine> mineMap;

    public MineCollection() {
        this.mineMap = new HashMap<>();
    }

    @Override
    public Mine getMine(String name) {
        return mineMap.get(name);
    }

    @Override
    public void addMine(Mine mine) {
        mineMap.put(mine.getName(), mine);
    }

    @Override
    public void removeMine(Mine mine) {
        mineMap.put(mine.getName(), mine);
    }

    @Override
    public Map<String, Mine> getMineMap() {
        return mineMap;
    }
}
