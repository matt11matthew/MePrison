package me.matthewe.meprison.player;

import me.matthewe.meprison.chat.rank.Rank;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface RankHolder {
    Rank getRank();
    void setRank(Rank rank);
}
