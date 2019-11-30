package com.hofill.deathrecovery;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.hofill.deathrecovery.commands.Deaths;
import com.hofill.deathrecovery.events.PlayerDeath;

public class Main extends JavaPlugin {

	public void onEnable() {
		registerConfig();
		registerDeathConfig();
		registerEvents();
		registerCommands();
	}

	public static void tellConsole(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg);
	}

	public void registerCommands() {
		getCommand("deaths").setExecutor(new Deaths());
	}

	public void registerAutoComplete() {

	}

	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
	}

	public void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}

	public void registerDeathConfig() {
		ConfigManager.setup();
		ConfigManager.getConfig().options().copyDefaults(true);
		ConfigManager.saveConfig();
	}

}
