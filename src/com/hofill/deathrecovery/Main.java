package com.hofill.deathrecovery;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {

	public void onEnable() {
		registerConfig();
	}

	public static void tellConsole(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg);
	}

	public void registerCommands() {

	}

	public void registerAutoComplete() {

	}

	public void registerEvents() {

	}

	public void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		ConfigManager.setup();
		ConfigManager.get().options().copyDefaults(true);
	}

}
