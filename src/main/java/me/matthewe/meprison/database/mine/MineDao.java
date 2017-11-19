package me.matthewe.meprison.database.mine;

import me.matthewe.meprison.mine.Mine;

import java.util.List;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface MineDao {
    List<Mine> getMineList();
    void createMine(Mine mine);
    void removeMine(Mine mine);
    Mine getMine(String name);
    boolean isMine(String name);

}
