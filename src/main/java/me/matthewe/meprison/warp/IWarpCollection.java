package me.matthewe.meprison.warp;

import java.util.Map;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface IWarpCollection {
    Warp getWarp(String name);

    void addWarp(Warp warp);

    void removeWarp(Warp warp);

    Map<String, Warp> getWarpMap();

}
