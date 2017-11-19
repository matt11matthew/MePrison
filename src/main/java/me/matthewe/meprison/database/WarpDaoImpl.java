package me.matthewe.meprison.database;

import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.utils.LocationUtils;
import me.matthewe.meprison.warp.Warp;
import me.matthewe.meprison.warp.WarpCollection;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class WarpDaoImpl implements WarpDao {
    private WarpCollection warpCollection;
    private final String INSERT_WARP = "INSERT INTO meprison.meprison_warps VALUES(NULL,?,?,?) ON DUPLICATE KEY UPDATE name=?";
    private final String SELECT_ALL_WARPS = "SELECT * FROM meprison.meprison_warps";
    private final String DELETE_WARP = "DELETE * FROM meprison.meprison_warps WHERE name=?";
    private int rowCount;

    public WarpDaoImpl() {
        warpCollection = new WarpCollection();
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();

                PreparedStatement deleteStatement = connection.prepareStatement("SELECT COUNT(*) FROM meprison.meprison_warps");
                PreparedStatement selectAllStatement = connection.prepareStatement(SELECT_ALL_WARPS);
                ResultSet resultSet = deleteStatement.executeQuery();
                if (resultSet.next()) {
                    rowCount = resultSet.getInt("count(*)");
                }
                resultSet.close();
                ResultSet resultSet1 = selectAllStatement.executeQuery();
                while (resultSet1.next()) {
                    String location = resultSet1.getString("location");
                    int seconds = resultSet1.getInt("timeSeconds");
                    String name = resultSet1.getString("name");
                    Warp warp = new Warp(name, LocationUtils.getPlayerLocationFromString(location), seconds);
                    warpCollection.addWarp(warp);
                    rowCount = warpCollection.getWarpMap().values().size();
                }
                Bukkit.getLogger().log(Level.INFO, "LOADED " + rowCount + " WARPS");
                for (Warp warp : warpCollection.getWarpMap().values()) {
                    Bukkit.getLogger().log(Level.INFO, " > " + warp.getName());
                }
                resultSet1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public List<Warp> getWarpList() {
        return new ArrayList<>(warpCollection.getWarpMap().values());
    }

    @Override
    public void createWarp(Warp warp) {
        warpCollection.addWarp(warp);
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(INSERT_WARP);
                insertStatement.setString(1, warp.getName());
                insertStatement.setString(2, LocationUtils.getLocationStringFromPlayerLocation(warp.getLocation()));
                insertStatement.setInt(3, warp.getWarpTime());
                insertStatement.setString(4, warp.getName());
                insertStatement.execute();
                rowCount++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void removeWarp(Warp warp) {
        warpCollection.removeWarp(warp);
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();
                PreparedStatement deleteStatement = connection.prepareStatement(DELETE_WARP);
                deleteStatement.setString(1, warp.getName());
                deleteStatement.execute();
                rowCount--;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Warp getWarp(String name) {
        return warpCollection.getWarp(name);
    }

    public int getRowCount() {
        return rowCount;
    }

    @Override
    public boolean isWarp(String name) {
        Map<String, Warp> warpMap = warpCollection.getWarpMap();
        for (String warpName : warpMap.keySet()) {
           if (name.equalsIgnoreCase(warpName)) {
               return true;
           }
        }
        return false;
    }
}
