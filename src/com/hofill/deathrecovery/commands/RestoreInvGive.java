package com.hofill.deathrecovery.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hofill.deathrecovery.ConfigManager;

import net.md_5.bungee.api.ChatColor;

public class RestoreInvGive implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(label.equalsIgnoreCase("restoreinvgive") || label.equalsIgnoreCase("rig")) {
			if (args.length >= 2 && args.length <= 3) {
				if (sender instanceof Player) {
					if (parseInt(args[1]) != -1) {
						Player player = (Player) sender;
						if (player.hasPermission("deathrecovery.restoregive")) {
							@SuppressWarnings("deprecation")
							OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
							String playerUUID = offlinePlayer.getUniqueId().toString();
							ConfigurationSection sectionDeaths = ConfigManager.getConfig()
									.getConfigurationSection("players." + playerUUID + "." + args[1] + ".items");
							// Check if there are any items in the death inventory.
							if (sectionDeaths != null) {
								// Check if player is online.
								if(offlinePlayer.getPlayer().isOnline()) {
									// Check if his inventory is empty.
									if(getFull(offlinePlayer.getPlayer().getInventory())) {
										// Add items to the player's inventory
										for (String items : sectionDeaths.getKeys(false)) {
											int itemIndex = parseInt(items);
											ItemStack itemToAdd = ConfigManager.getConfig().getItemStack("players." + playerUUID + "." + args[1] + ".items." + items);
											offlinePlayer.getPlayer().getInventory().setItem(itemIndex, itemToAdd);
										}
									} else {
										player.sendMessage(ChatColor.RED + args[0] + "'s inventory is not empty!");
									}
								} else {
									
								}
							} else {
								player.sendMessage(ChatColor.RED + "The player or the death ID does not exist!");
								return true;								
							}
							
						} else {
							player.sendMessage(ChatColor.RED + "You don't have permission!");
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "The death ID is a number bigger than 0!");
						return false;
					}
				} else {
					sender.sendMessage("Only players can use this command!");
					return true;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean getFull(Inventory inv) {
		for(ItemStack item : inv.getContents())
		{
		    if(item != null)
		      return false;
		}
		return true;
	}
	
	private int parseInt(String string) {
		int x = -1;
		try {
			x = Integer.parseInt(string);
		} catch (Exception ex) {
		}
		return x;
	}

	
}
