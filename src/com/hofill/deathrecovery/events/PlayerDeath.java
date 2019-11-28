package com.hofill.deathrecovery.events;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.hofill.deathrecovery.ConfigManager;
import com.hofill.deathrecovery.Main;

public class PlayerDeath implements Listener{

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		ConfigurationSection sec = ConfigManager.getConfig().getConfigurationSection("players");
		ArrayList<String> storedPlayers = new ArrayList<String>();
		//Get stored players
		if(sec != null) {
			for(String key : sec.getKeys(false)) {
				storedPlayers.add(key);
			}
		}
		//Check to see if player is in the deaths.yml file, if not add him
		if(storedPlayers.isEmpty() || !storedPlayers.contains(player.getUniqueId().toString())) {
			ConfigManager.getConfig().set("players." + player.getUniqueId().toString() +"." + "1.death_x", player.getLocation().getBlockX());
			ConfigManager.getConfig().set("players." + player.getUniqueId().toString() +"." + "1.death_y", player.getLocation().getBlockY());
			ConfigManager.getConfig().set("players." + player.getUniqueId().toString() +"." + "1.death_z", player.getLocation().getBlockZ());
			ConfigManager.saveConfig();
			ConfigManager.reloadConfig();
		}
		
		
		
	}
	
}
