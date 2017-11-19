package me.matthewe.meprison.lanaguage;

import me.matthewe.meprison.config.Config;
import org.bukkit.ChatColor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by Matthew E on 11/19/2017.
 */
public class LangConfig extends Config {
    public static String prefix = "&a&lMePrison&7";
    public static String rankDoesntExist= "%prefix% &cThe rank %rankName% doesn't exist.";
    public static String yourLanguage = "&aYou're language is set to &7%language%&a.";
    public static String mineAlreadyExists="&cThe mine %mine% already exists.";

    public LangConfig(String path) {
        super(path);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void init() {
        super.init();
        try {
            for (Field field : this.getClass().getFields()) {
                boolean isIgnore = false;
                for (Annotation annotation : field.getAnnotations()) {
                    if (annotation.annotationType().getSimpleName().equalsIgnoreCase("IgnoreField")) {
                        isIgnore = true;
                    }
                }
                if (isIgnore) {
                    continue;
                }
                if (field.getGenericType().getTypeName().contains("String")) {
                    Object o = field.get(field.getGenericType());
                    String string = (String) o;
                    string = string.replaceAll("%prefix%", prefix);
                    field.set(field.getGenericType(), ChatColor.translateAlternateColorCodes('&', string));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
