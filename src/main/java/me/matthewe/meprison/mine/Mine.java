package me.matthewe.meprison.mine;

import org.bukkit.Location;

import java.util.List;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class Mine {
    private String name;
    private Location pos1;
    private Location pos2;
    private List<MineBlock> mineBlockList;
    private String region;
    private Location teleportLocation;

    public Mine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public Mine setPos1(Location pos1) {
        this.pos1 = pos1;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Location getPos2() {
        return pos2;
    }

    public Mine setPos2(Location pos2) {
        this.pos2 = pos2;
        return this;
    }

    public List<MineBlock> getMineBlockList() {
        return mineBlockList;
    }

    public Mine setMineBlockList(List<MineBlock> mineBlockList) {
        this.mineBlockList = mineBlockList;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }
}
