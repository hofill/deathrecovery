package com.hofill.deathrecovery.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hofill.deathrecovery.ConfigManager;
import com.hofill.deathrecovery.commands.RestoreInvGive;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(getFull(player.getInventory())) {
			String playerUUID = player.getUniqueId().toString();
			ConfigurationSection sectionPlayers = ConfigManager.getConfig()
					.getConfigurationSection("offline_players." + playerUUID);
			if (sectionPlayers != null) {
				String death_id = ConfigManager.getConfig().getString("offline_players." + playerUUID + ".death_id");
				String inv_name = ConfigManager.getConfig().getString("offline_players." + playerUUID + ".inv_name");
				ConfigurationSection sectionItems = ConfigManager.getConfig()
						.getConfigurationSection("players." + inv_name + "." + death_id + ".items");
				if(sectionItems != null) {
					RestoreInvGive.fillInv(sectionItems, playerUUID, player, death_id);
					player.sendMessage(ChatColor.GREEN + "Successfully recovered your inventory!");
					removeFromYAML(playerUUID);
				} else {
					player.sendMessage(ChatColor.RED + "Your death inventory expired or is invalid!");
					removeFromYAML(playerUUID);
				}
				
			}
		} else {
			player.sendMessage(ChatColor.RED + "Could not restore your death inventory because your inventory is full!");
			player.sendMessage(ChatColor.RED + "Clear out your inventory and log back in!");
		}
	}
	
	private void removeFromYAML(String playerUUID) {
		ConfigManager.getConfig().getConfigurationSection("offline_players").set(playerUUID, null);
		ConfigManager.saveConfig();
		ConfigManager.reloadConfig();
	}

	private boolean getFull(Inventory inv) {
		for (ItemStack item : inv.getContents()) {
			if (item != null)
				return false;
		}
		return true;
	}
	
}
