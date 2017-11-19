package me.matthewe.meprison.database.mine;

import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.mine.Mine;
import me.matthewe.meprison.mine.MineBlock;
import me.matthewe.meprison.mine.MineCollection;
import me.matthewe.meprison.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;

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
public class MineDaoImpl implements MineDao {
    private MineCollection mineCollection;
    private final String INSERT_MINE = "INSERT INTO meprison.meprison_mines VALUES(NULL,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE name=?";
    private final String SELECT_ALL_MINES = "SELECT * FROM meprison.meprison_mines";
    private final String DELETE_MINE= "DELETE * FROM meprison.meprison_mines WHERE name=?";
    private int rowCount;

    public MineDaoImpl() {
        mineCollection = new MineCollection();
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();

                PreparedStatement deleteStatement = connection.prepareStatement("SELECT COUNT(*) FROM meprison.meprison_mines");
                PreparedStatement selectAllStatement = connection.prepareStatement(SELECT_ALL_MINES);
                ResultSet resultSet = deleteStatement.executeQuery();
                if (resultSet.next()) {
                    rowCount = resultSet.getInt("count(*)");
                }
                resultSet.close();
                ResultSet resultSet1 = selectAllStatement.executeQuery();
                while (resultSet1.next()) {
                    String pos1 = resultSet1.getString("pos1");
                    String pos2 = resultSet1.getString("pos2");
                    String blocks = resultSet1.getString("blocks");
                    String name = resultSet1.getString("name");
                    String region = resultSet1.getString("region");
                    String teleportLocation = resultSet1.getString("teleportLocation");
                    Mine mine = new Mine(name);
                    List<MineBlock> mineBlockList = new ArrayList<>();
                    if (blocks.contains(",")) {
                        for (String s : blocks.split(",")) {
                        //stone@0:10%,sand@0:5%,emerald@0:5%
                            String type = s.split("@")[0];
                            short data = Short.parseShort(s.split("@")[1]);
                            int percent = Integer.parseInt(s.split(":")[1].split("%")[0].trim());
                            mineBlockList.add(new MineBlock(Material.getMaterial(type.toUpperCase()), data, percent));
                        }
                    }
                    mine.setPos1(LocationUtils.getBlockLocationFromString(pos1));
                    mine.setPos2(LocationUtils.getBlockLocationFromString(pos2));
                    mine.setRegion(region);
                    mine.setTeleportLocation(LocationUtils.getPlayerLocationFromString(teleportLocation));
                    mine.setMineBlockList(mineBlockList);
                    mineCollection.addMine(mine);
                    rowCount = mineCollection.getMineMap().values().size();
                }
                Bukkit.getLogger().log(Level.INFO, "------------------------------------");
                Bukkit.getLogger().log(Level.INFO, "LOADED " + rowCount + " MINES");
                for (Mine mine : mineCollection.getMineMap().values()) {
                    Bukkit.getLogger().log(Level.INFO, " > " + mine.getName());
                }
                Bukkit.getLogger().log(Level.INFO, "------------------------------------");
                resultSet1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public List<Mine> getMineList() {
        return new ArrayList<>(mineCollection.getMineMap().values());
    }

    @Override
    public void createMine(Mine mine) {
        mineCollection.addMine(mine);
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(INSERT_MINE);
                insertStatement.setString(1, mine.getName());
                insertStatement.setString(2, LocationUtils.getLocationStringFromBlockLocation(mine.getPos1()));
                insertStatement.setString(3, LocationUtils.getLocationStringFromBlockLocation(mine.getPos2()));
                String blockString = "";
                for (MineBlock mineBlock : mine.getMineBlockList()) {
                    blockString+=mineBlock.getMaterial().toString() + "@" + mineBlock.getData() + ":" + mineBlock.getPercentage() + "%,";
                }
                if (blockString.endsWith(",")) {
                    blockString = blockString.substring(0, blockString.length()-1);
                }
                insertStatement.setString(4, blockString);
                insertStatement.setString(5, mine.getRegion());
                insertStatement.setString(6, LocationUtils.getLocationStringFromPlayerLocation(mine.getTeleportLocation()));
                insertStatement.setString(7, mine.getName());
                insertStatement.execute();
                rowCount++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void removeMine(Mine mine) {
        mineCollection.removeMine(mine);
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();
                PreparedStatement deleteStatement = connection.prepareStatement(DELETE_MINE);
                deleteStatement.setString(1, mine.getName());
                deleteStatement.execute();
                rowCount--;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Mine getMine(String name) {
        return mineCollection.getMine(name);
    }

    public int getRowCount() {
        return rowCount;
    }

    @Override
    public boolean isMine(String name) {
        Map<String, Mine> mineMap = mineCollection.getMineMap();
        for (String mineName : mineMap.keySet()) {
           if (name.equalsIgnoreCase(mineName)) {
               return true;
           }
        }
        return false;
    }
}
