package me.matthewe.meprison.database.player;

import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.chat.rank.Rank;
import me.matthewe.meprison.lanaguage.LanguageConfig;
import me.matthewe.meprison.player.*;
import me.matthewe.meprison.player.collection.OfflinePrisonPlayerCollection;
import me.matthewe.meprison.player.collection.OnlinePrisonPlayerCollection;
import me.matthewe.meprison.warp.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class PrisonPlayerDaoImpl implements PrisonPlayerDao {
    private OnlinePrisonPlayerCollection onlinePlayerCollection;
    private OfflinePrisonPlayerCollection offlinePlayerCollection;
    private final String INSERT_PLAYER = "INSERT INTO meprison.meprison_players VALUES(NULL,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE name=?";
    private final String SELECT_PLAYER = "SELECT * FROM meprison.meprison_players WHERE uuid=?";
    private final String UPDATE_PLAYER = "UPDATE meprison.meprison_players SET name=?, balance=?, rank=?, statistics=?, language=? WHERE uuid=?";
    private final String DELETE_PLAYER = "DELETE * FROM meprison.meprison_players WHERE uuid=?";
    private int rowCount;

    public PrisonPlayerDaoImpl() {
        onlinePlayerCollection = new OnlinePrisonPlayerCollection();
        offlinePlayerCollection = new OfflinePrisonPlayerCollection();
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();

                PreparedStatement deleteStatement = connection.prepareStatement("SELECT COUNT(*) FROM meprison.meprison_players");
                ResultSet resultSet = deleteStatement.executeQuery();
                if (resultSet.next()) {
                    rowCount = resultSet.getInt("count(*)");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(MePrison.getInstance(), () -> {
            List<OfflinePrisonPlayer> collect = offlinePlayerCollection.getPrisonPlayerMap()
                    .values()
                    .stream()
                    .filter(offlinePrisonPlayer -> System.currentTimeMillis() - offlinePrisonPlayer.getTime() > TimeUnit.MINUTES.toMillis(1))
                    .collect(Collectors.toList());
            collect.forEach(offlinePrisonPlayer -> offlinePlayerCollection.removePlayer(offlinePrisonPlayer));

        }, 500L, 500L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(MePrison.getInstance(), () -> {
            onlinePlayerCollection.getPrisonPlayerMap()
                    .values()
                    .stream()
                    .filter(onlinePrisonPlayer -> onlinePrisonPlayer.isWarping() && onlinePrisonPlayer.getWarpCooldown() != null)
                    .forEach(onlinePrisonPlayer -> {
                        WarpCooldown warpCooldown = onlinePrisonPlayer.getWarpCooldown();
                        if (warpCooldown.isDone()) {
                            Warp warp = warpCooldown.getWarp();
                            Player player = Bukkit.getPlayer(onlinePrisonPlayer.getUuid());
                            if (player != null && (player.isOnline())) {
                                player.teleport(warp.getLocation());
                                onlinePrisonPlayer.setWarpCooldown(null);
                                onlinePrisonPlayer.setWarping(false);
                                player.sendMessage(ChatColor.GREEN + "Warped to " + ChatColor.GRAY + warp.getName() + ChatColor.GREEN + ".");
                            }
                        } else {
                            Warp warp = warpCooldown.getWarp();
                            Player player = Bukkit.getPlayer(onlinePrisonPlayer.getUuid());
                            if (player != null && (player.isOnline())) {
                                onlinePrisonPlayer.setWarpCooldown(warpCooldown);
                                onlinePrisonPlayer.setWarping(true);
                                player.sendMessage(ChatColor.GREEN + "Warping in " + ChatColor.GRAY + warpCooldown.getSecondsLeft() + "s" + ChatColor.GREEN + ".");
                            }
                        }
                    });

        }, 20L, 20L);
    }

    @Override
    public List<OnlinePrisonPlayer> getPrisonPlayerList() {
        return onlinePlayerCollection.getPrisonPlayerList();
    }

    @Override
    public void updatePrisonPlayer(OnlinePrisonPlayer prisonPlayer, Runnable success, Runnable failure) {
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();
                PreparedStatement savePreparedStatement = connection.prepareStatement(UPDATE_PLAYER);
                savePreparedStatement.setString(1, prisonPlayer.getName());
                savePreparedStatement.setDouble(2, prisonPlayer.getBalance());
                savePreparedStatement.setString(3, prisonPlayer.getRank().toString());
             savePreparedStatement.setString(4, Base64.getEncoder().encodeToString(prisonPlayer.getStatisticJson().getBytes()));
             savePreparedStatement.setString(5, prisonPlayer.getLanguage());
                savePreparedStatement.setString(6, prisonPlayer.getUuid().toString());
                savePreparedStatement.execute();
                onlinePlayerCollection.removePlayer(prisonPlayer);
                if (success != null) {
                    success.run();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (failure != null) {
                    failure.run();
                }
            }
        });
    }

    @Override
    public void updatePrisonPlayer(OnlinePrisonPlayer prisonPlayer, Runnable success) {
        updatePrisonPlayer(prisonPlayer, success, null);
    }

    @Override
    public void updatePrisonPlayer(OnlinePrisonPlayer prisonPlayer) {
        updatePrisonPlayer(prisonPlayer, null, null);
    }

    @Override
    public void deletePrisonPlayer(OnlinePrisonPlayer prisonPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();
                PreparedStatement deleteStatement = connection.prepareStatement(DELETE_PLAYER);
                deleteStatement.setString(1, prisonPlayer.getUuid().toString());
                deleteStatement.execute();
                onlinePlayerCollection.removePlayer(prisonPlayer);
                rowCount--;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public void requestPrisonPlayer(Player player, Consumer<OnlinePrisonPlayer> success) {
        long time = System.currentTimeMillis();
        if (onlinePlayerCollection.getPrisonPlayerMap().containsKey(player.getUniqueId())) {
            if (success != null) {
                success.accept(onlinePlayerCollection.getPrisonPlayerMap().get(player.getUniqueId()));
            }
            Bukkit.getLogger().log(Level.INFO, "Requested " + player.getName() + ":" + player.getUniqueId() + " in " + (System.currentTimeMillis()-time) + "ms");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection = MePrison.getInstance().getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(SELECT_PLAYER);


                selectStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    OnlinePrisonPlayer prisonPlayer = new OnlinePrisonPlayer(player.getUniqueId(), player.getName());
                    prisonPlayer.setBalance(resultSet.getDouble("balance"));
                    prisonPlayer.setFirstSession(false);
                    prisonPlayer.setRank(Rank.valueOf(resultSet.getString("rank")));
                    prisonPlayer.setLanguage(resultSet.getString("language"));
                    prisonPlayer.loadStatistics(resultSet.getString("statistics"));
                    onlinePlayerCollection.addPlayer(prisonPlayer);
                    if (success != null) {
                        success.accept(prisonPlayer);
                    }
                } else {
                    PreparedStatement insertStatement = connection.prepareStatement(INSERT_PLAYER);
                    insertStatement.setString(1, player.getUniqueId().toString());
                    insertStatement.setString(2, player.getName());
                    insertStatement.setDouble(3, 0.0D);
                    insertStatement.setString(4, Rank.NORMAL.toString());
                    insertStatement.setString(5, Base64.getEncoder().encodeToString("{\"statistics\":{\"deaths\":[],\"kills\":[],\"joinCount\":0,\"text\":\"test\"}}".getBytes()));
                    insertStatement.setString(6, LanguageConfig.language);
                    insertStatement.setString(7, player.getName());
                    insertStatement.execute();
                    OnlinePrisonPlayer prisonPlayer = new OnlinePrisonPlayer(player.getUniqueId(), player.getName());
                    prisonPlayer.setBalance(0.0D);
                    prisonPlayer.setFirstSession(true);
                    prisonPlayer.setRank(Rank.NORMAL);
                    prisonPlayer.setLanguage(LanguageConfig.language);
                    rowCount++;
                    onlinePlayerCollection.addPlayer(prisonPlayer);
                    if (success != null) {
                        success.accept(prisonPlayer);
                    }
                }
                resultSet.close();
                Bukkit.getLogger().log(Level.INFO, "Requested " + player.getName() + ":" + player.getUniqueId() + " in " + (System.currentTimeMillis()-time) + "ms");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public void requestOfflinePrisonPlayer(UUID uuid, Consumer<OfflinePrisonPlayer> success, Runnable failure) {
        if (onlinePlayerCollection.getPrisonPlayerMap().containsKey(uuid)) {
            OfflinePrisonPlayer offlinePrisonPlayer = new OfflinePrisonPlayer(onlinePlayerCollection.getPrisonPlayerMap().get(uuid));
            if (!offlinePlayerCollection.getPrisonPlayerMap().containsKey(uuid)) {
                offlinePlayerCollection.addPlayer(offlinePrisonPlayer);
            }
            if (success != null) {
                success.accept(offlinePrisonPlayer);
            }
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(MePrison.getInstance(), () -> {
            try {
                Connection connection =  MePrison.getInstance().getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(SELECT_PLAYER);
                selectStatement.setString(1, uuid.toString());
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    OfflinePrisonPlayer prisonPlayer = new OfflinePrisonPlayer(uuid);
                    prisonPlayer.setName(resultSet.getString("name"));
                    prisonPlayer.setBalance(resultSet.getDouble("balance"));
                    prisonPlayer.setRank(Rank.valueOf(resultSet.getString("rank")));
                    prisonPlayer.loadStatistics(resultSet.getString("statistics"));
                    prisonPlayer.setLanguage(resultSet.getString("language"));
                    if (!offlinePlayerCollection.getPrisonPlayerMap().containsKey(uuid)) {
                        offlinePlayerCollection.addPlayer(prisonPlayer);
                    }
                    prisonPlayer.setFirstSession(false);
                    if (success != null) {
                        success.accept(prisonPlayer);
                    }
                } else {
                    if (failure != null) {
                        failure.run();

                    }
                }
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
                if (failure != null) {
                    failure.run();

                }
            }
        });
    }

    @Override
    public void requestOfflinePrisonPlayer(UUID uuid, Consumer<OfflinePrisonPlayer> success) {
       requestOfflinePrisonPlayer(uuid, success, null);
    }

    @Override
    public void requestOfflinePrisonPlayer(String name, Consumer<OfflinePrisonPlayer> success) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (offlinePlayer!=null) {
            UUID uniqueId = offlinePlayer.getUniqueId();
            requestOfflinePrisonPlayer(uniqueId, success);
        }
    }

    @Override
    public void requestOfflinePrisonPlayer(String name, Consumer<OfflinePrisonPlayer> success, Runnable failure) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (offlinePlayer!=null) {
            UUID uniqueId = offlinePlayer.getUniqueId();
            requestOfflinePrisonPlayer(uniqueId, success, failure);
        }
    }

    @Override
    public void requestGenericPrisonPlayer(UUID uuid, Consumer<PrisonPlayer> success) {

    }
}
