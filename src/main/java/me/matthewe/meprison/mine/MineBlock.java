package me.matthewe.meprison.mine;

import org.bukkit.Material;

/**
 * Created by Matthew E on 11/19/2017.
 */
public class MineBlock {
    private Material material;
    private short data;
    private int percentage;

    public MineBlock(Material material, short data, int percentage) {
        this.material = material;
        this.data = data;
        this.percentage = percentage;
    }

    public Material getMaterial() {
        return material;
    }

    public MineBlock setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public short getData() {
        return data;
    }

    public MineBlock setData(short data) {
        this.data = data;
        return this;
    }

    public int getPercentage() {
        return percentage;
    }

    public MineBlock setPercentage(int percentage) {
        this.percentage = percentage;
        return this;
    }
}
