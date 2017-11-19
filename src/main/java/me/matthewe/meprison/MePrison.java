package me.matthewe.meprison;

import co.aikar.commands.BukkitCommandManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.zaxxer.hikari.HikariDataSource;
import me.matthewe.meprison.commands.*;
import me.matthewe.meprison.database.WarpDao;
import me.matthewe.meprison.database.WarpDaoImpl;
import me.matthewe.meprison.database.mine.MineDao;
import me.matthewe.meprison.database.mine.MineDaoImpl;
import me.matthewe.meprison.database.player.PrisonPlayerDao;
import me.matthewe.meprison.database.player.PrisonPlayerDaoImpl;
import me.matthewe.meprison.lanaguage.LanguageConfig;
import me.matthewe.meprison.listeners.player.ChatListener;
import me.matthewe.meprison.listeners.player.PlayerJoinListener;
import me.matthewe.meprison.listeners.player.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public final class MePrison extends JavaPlugin {
    private static MePrison instance;
    private HikariDataSource hikari;
    private static PrisonPlayerDao prisonPlayerDao;
    private static WarpDao warpDao;
    private static MineDao mineDao;
    private File databaseFile;
    private FileConfiguration databaseConfig;
    private static BukkitCommandManager commandManager;
    private Connection connection;
    private static WorldEditPlugin worldEditPlugin;

    public static MePrison getInstance() {
        return instance;
    }

    public static PrisonPlayerDao getPrisonPlayerDao() {
        return prisonPlayerDao;
    }

    public static BukkitCommandManager getCommandManager() {
        return commandManager;
    }

    public static WarpDao getWarpDao() {
        return warpDao;
    }

    public static MineDao getMineDao() {
        return mineDao;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfigurations();
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", databaseConfig.getString("host"));
        hikari.addDataSourceProperty("port", databaseConfig.getInt("port"));
        hikari.addDataSourceProperty("databaseName",  databaseConfig.getString("databaseName"));
        hikari.addDataSourceProperty("user", databaseConfig.getString("user"));
        hikari.addDataSourceProperty("password", databaseConfig.getString("password"));
        refreshConnection();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, this::refreshConnection, 36000L, 36000L);
        registerListeners();
        registerCommands();
        prisonPlayerDao = new PrisonPlayerDaoImpl();
        warpDao = new WarpDaoImpl();
        mineDao = new MineDaoImpl();
        if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
            worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        }
        new LanguageConfig();
    }

    private void refreshConnection() {
        try {
            connection = hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new BalanceCommand().setExceptionHandler((command, registeredCommand, sender, args, t) -> true));
        commandManager.registerCommand(new WarpCommand().setExceptionHandler((command, registeredCommand, sender, args, t) -> true));
        commandManager.registerCommand(new RankCommand().setExceptionHandler((command, registeredCommand, sender, args, t) -> true));
        commandManager.registerCommand(new MineCommand().setExceptionHandler((command, registeredCommand, sender, args, t) -> true));
        commandManager.registerCommand(new LanguageCommand().setExceptionHandler((command, registeredCommand, sender, args, t) -> true));

    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hikari.close();
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public File getDatabaseFile() {
        return databaseFile;
    }

    public FileConfiguration getDatabaseConfig() {
        return databaseConfig;
    }

    private void loadConfigurations() {
        this.databaseFile = new File(getDataFolder(), "database.yml");
        if (!this.databaseFile.exists()) {
            this.saveResource("database.yml", false);
        }
        this.databaseConfig = YamlConfiguration.loadConfiguration(this.databaseFile);
    }

    private void saveConfigurations() {
        try {
            this.databaseConfig.save(this.databaseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public  static WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }
}
