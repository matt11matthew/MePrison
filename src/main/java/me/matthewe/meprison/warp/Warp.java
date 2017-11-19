package me.matthewe.meprison.warp;

import org.bukkit.Location;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class Warp {
    private String name;
    private Location location;
    private int warpTime;

    public Warp(String name, Location location, int warpTime) {
        this.name = name;
        this.location = location;
        this.warpTime = warpTime;
    }

    public Warp(String name) {
        this.name = name;
    }

    public Warp setName(String name) {
        this.name = name;
        return this;
    }

    public Warp setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Warp setWarpTime(int warpTime) {
        this.warpTime = warpTime;
        return this;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getWarpTime() {
        return warpTime;
    }

}
