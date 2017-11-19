package me.matthewe.meprison.player;

import me.matthewe.meprison.warp.Warp;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface WarpCooldown {
    Warp getWarp();

    long getSecondsLeft();

    boolean isDone();
}
