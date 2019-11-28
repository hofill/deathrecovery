package com.hofill.deathrecovery;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.hofill.deathrecovery.Main;

public class ConfigManager {

	private static Main plugin = Main.getPlugin(Main.class);
	private static File file;
	private static FileConfiguration deathsFile;

	public static void setup() {
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

	public static FileConfiguration get() {
		return deathsFile;
	}

	public static void save() {
		plugin.saveResource("deaths.yml", false);
	}

	public static void reload() {
		deathsFile = YamlConfiguration.loadConfiguration(file);
	}

}
