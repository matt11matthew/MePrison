package me.matthewe.meprison.utils;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class LocationUtils {
    public static Location getPlayerLocationFromString(String location) {
        String[] data = location.split(",");
        World world = Bukkit.getWorld(data[0]);
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double z = Double.parseDouble(data[3]);
        float yaw = Float.parseFloat(data[4]);
        float pitch = Float.parseFloat(data[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getLocationStringFromBlockLocation(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    public static String getLocationStringFromPlayerLocation(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    public static Location getBlockLocationFromString(String location) {
        String[] data = location.split(",");
        World world = Bukkit.getWorld(data[0]);
        int x = Integer.parseInt(data[1]);
        int y = Integer.parseInt(data[2]);
        int z = Integer.parseInt(data[3]);
        return new Location(world, x, y, z);
    }
}
