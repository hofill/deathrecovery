package com.hofill.deathrecovery;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.hofill.deathrecovery.Main;

public class ConfigManager {

    private Main plugin = Main.getPlugin(Main.class);
    private File file;
    private FileConfiguration deathsFile;

    public void setup() {
        file = new File(plugin.getDataFolder(), "deaths.yml");

        if (!file.exists()) {
            try {
                plugin.saveResource("deaths.yml", false);
            } catch (Exception ex) {
                Main.tellConsole("Cannot create deaths.yml!");
            }
        }

        deathsFile = YamlConfiguration.loadConfiguration(file);

    }

    public FileConfiguration getConfig() {
        return deathsFile;
    }

    public void saveConfig() {
        try {
            deathsFile.save(file);
        } catch (Exception ex) {
        }
    }

    public void reloadConfig() {
        deathsFile = YamlConfiguration.loadConfiguration(file);
    }

}
