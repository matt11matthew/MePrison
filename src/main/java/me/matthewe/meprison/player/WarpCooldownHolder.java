package me.matthewe.meprison.player;

/**
 * Created by Matthew E on 11/18/2017.
 */
public interface WarpCooldownHolder {
   WarpCooldown getWarpCooldown();

   void setWarpCooldown(WarpCooldown warpCooldown);

   boolean isWarping();

   void setWarping(boolean warping);
}
