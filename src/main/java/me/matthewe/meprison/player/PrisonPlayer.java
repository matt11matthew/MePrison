package me.matthewe.meprison.player;

import java.util.UUID;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface PrisonPlayer extends BalanceHolder, WarpCooldownHolder, RankHolder, LanguageHolder {
    String getName();
    UUID getUuid();
    boolean isFirstSession();
    void setFirstSession(boolean firstSession);

    void loadStatistics(String statistics);

    String getStatisticJson();
}
