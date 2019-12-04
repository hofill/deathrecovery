package com.hofill.deathrecovery;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.hofill.deathrecovery.commands.Deaths;
import com.hofill.deathrecovery.commands.RestoreInvGive;
import com.hofill.deathrecovery.commands.RestoreInvSee;
import com.hofill.deathrecovery.events.InventoryClick;
import com.hofill.deathrecovery.events.PlayerDeath;
import com.hofill.deathrecovery.events.PlayerJoin;

public class Main extends JavaPlugin {

    private ConfigManager cfg;
    
	public void onEnable() {
		registerConfig();
		registerDeathConfig();
		registerEvents();
		registerCommands();
		new Timer(getConfig().getString("time_between_checks"), getConfig().getString("oldest_death_allowed"),cfg);
	}

	public static void tellConsole(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg);
	}

	public void registerCommands() {
		getCommand("deaths").setExecutor(new Deaths(cfg));
		getCommand("restoreinvsee").setExecutor(new RestoreInvSee(cfg));
		getCommand("restoreinvgive").setExecutor(new RestoreInvGive(cfg));
	}

	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerDeath(cfg), this);
		getServer().getPluginManager().registerEvents(new InventoryClick(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoin(cfg), this);
	}

	public void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}

	public void registerDeathConfig() {
	    cfg = new ConfigManager();
	    cfg.setup();
	    cfg.getConfig().options().copyDefaults(true);
	    cfg.saveConfig();
	}

}
