package me.matthewe.meprison.mine;

import java.util.Map;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface IMineCollection {
    Mine getMine(String name);

    void addMine(Mine mine);

    void removeMine(Mine mine);

    Map<String, Mine> getMineMap();

}
