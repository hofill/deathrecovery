package com.hofill.deathrecovery.events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.hofill.deathrecovery.ConfigManager;

public class PlayerDeath implements Listener {

	private static int deathId = 1;

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		ConfigurationSection sectionPlayers = ConfigManager.getConfig().getConfigurationSection("players");
		String playerUUID = player.getUniqueId().toString();
		ArrayList<String> storedPlayers = new ArrayList<String>();
		// Get stored players
		if (sectionPlayers != null) {
			for (String key : sectionPlayers.getKeys(false)) {
				storedPlayers.add(key);
			}
		}
		// Check to see if player is in the deaths.yml file. If so, get next available
		// death ID
		if (!storedPlayers.isEmpty() && storedPlayers.contains(playerUUID)) {
			ArrayList<String> deaths = new ArrayList<String>();
			ConfigurationSection sectionUUID = ConfigManager.getConfig()
					.getConfigurationSection("players." + playerUUID);
			if (sectionUUID != null) {
				for (String key : sectionUUID.getKeys(false)) {
					deaths.add(key);
				}
				deathId = parseInt(deaths.get(deaths.size() - 1)) + 1;
			}
		}
		// Fill up data in deaths.yml after death
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ConfigManager.getConfig().set("players." + playerUUID + "." + deathId + ".death_x",
				player.getLocation().getBlockX());
		ConfigManager.getConfig().set("players." + playerUUID + "." + deathId + ".death_y",
				player.getLocation().getBlockY());
		ConfigManager.getConfig().set("players." + playerUUID + "." + deathId + ".death_z",
				player.getLocation().getBlockZ());
		ConfigManager.getConfig().set("players." + playerUUID + "." + deathId + ".death_type", event.getDeathMessage());
		ConfigManager.getConfig().set("players." + playerUUID + "." + deathId + ".server_time", format.format(date));
		int inventoryCount = 0;
		int indexItems = 0;
		// Get items from inventory and add them to deaths.yml
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null) {
				ConfigManager.getConfig().set("players." + playerUUID + "." + deathId + ".items." + indexItems, i);
				inventoryCount++;
			}
			indexItems++;
		}
		ConfigManager.getConfig().set("players." + playerUUID + "." + deathId + ".item_count", inventoryCount);
		ConfigManager.saveConfig();
		ConfigManager.reloadConfig();

	}

	private int parseInt(String string) {
		int x = 0;
		try {
			x = Integer.parseInt(string);
		} catch (Exception ex) {
		}
		return x;
	}

}
