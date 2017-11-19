package me.matthewe.meprison.player;

import me.matthewe.meprison.warp.Warp;

import java.util.concurrent.TimeUnit;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class WarpCooldownImpl implements WarpCooldown {
    private Warp warp;
    private long time;

    public WarpCooldownImpl(Warp warp) {
        this.warp = warp;
        this.time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(warp.getWarpTime());
    }

    @Override
    public Warp getWarp() {
        return warp;
    }

    @Override
    public long getSecondsLeft() {
        if (System.currentTimeMillis() < time) {
           return TimeUnit.MILLISECONDS.toSeconds(time - System.currentTimeMillis());
        }
        return 0;
    }

    @Override
    public boolean isDone() {
        return getSecondsLeft() == 0;
    }
}
