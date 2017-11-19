package me.matthewe.meprison.lanaguage;

import me.matthewe.meprison.MePrison;
import me.matthewe.meprison.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Matthew E on 11/19/2017.
 */
public class LanguageConfig extends Config {
    public static String language = "us";

    @IgnoreField
    public static Map<String, LangConfig> languageConfigMap;
    @IgnoreField
    public static Map<UUID, String> playerLanguageMap;

    public LanguageConfig() {
        super("language.yml");
    }
    public static LangConfig getLanguage(){
        return languageConfigMap.get(language);
    }
    public static LangConfig getLanguage(CommandSender commandSender){
        return getLanguage();
    }

    public static LangConfig getLanguage(Player player){
        if (playerLanguageMap.containsKey(player.getUniqueId())) {
            String language = playerLanguageMap.get(player.getUniqueId());
            if (languageConfigMap.containsKey(language)) {
                return languageConfigMap.get(language);
            } else {
                playerLanguageMap.remove(player.getUniqueId());
            }
        }
        if (!playerLanguageMap.containsKey(player.getUniqueId())) {
            playerLanguageMap.put(player.getUniqueId(), language);
        }

        return languageConfigMap.get(playerLanguageMap.get(player.getUniqueId()));
    }
    @Override
    public void onLoad() {
        long time = System.currentTimeMillis();
        System.out.println("--------------------------------------");
        Bukkit.getLogger().log(Level.INFO,  "Language is " + language);
        languageConfigMap = new HashMap<>();
        playerLanguageMap = new HashMap<>();
        for (File file1 : Objects.requireNonNull(new File(MePrison.getInstance().getDataFolder() + "/languages/").listFiles())) {
            LangConfig config = new LangConfig("languages/" + file1.getName());
            languageConfigMap.put(config.getFile().getName().replaceAll("\\.yml", ""), config);
        }
        System.out.println("Loading Languages...");
        languageConfigMap.forEach((s, config) -> {
            System.out.println(" > " + s);
        });
        System.out.println("LOADED " + languageConfigMap.keySet().size() + " LANGUAGES IN " + (System.currentTimeMillis()-time) + "ms");
        System.out.println("--------------------------------------");

    }
}
