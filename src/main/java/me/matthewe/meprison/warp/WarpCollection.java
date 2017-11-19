package me.matthewe.meprison.warp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class WarpCollection implements IWarpCollection {
    private Map<String, Warp> warpMap;

    public WarpCollection() {
        this.warpMap = new HashMap<>();
    }

    @Override
    public Warp getWarp(String name) {
        return warpMap.get(name);
    }

    @Override
    public void addWarp(Warp warp) {
        warpMap.put(warp.getName(), warp);
    }

    @Override
    public void removeWarp(Warp warp) {
        warpMap.put(warp.getName(), warp);
    }

    @Override
    public Map<String, Warp> getWarpMap() {
        return warpMap;
    }
}
