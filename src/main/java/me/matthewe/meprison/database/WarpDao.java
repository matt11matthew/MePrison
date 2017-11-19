package me.matthewe.meprison.database;

import me.matthewe.meprison.warp.Warp;

import java.util.List;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface WarpDao {
    List<Warp> getWarpList();
    void createWarp(Warp warp);
    void removeWarp(Warp warp);
    Warp getWarp(String name);
    int getRowCount();
    boolean isWarp(String name);

}
