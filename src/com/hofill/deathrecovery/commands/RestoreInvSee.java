package com.hofill.deathrecovery.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.hofill.deathrecovery.ConfigManager;

import net.md_5.bungee.api.ChatColor;

public class RestoreInvSee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (label.equalsIgnoreCase("restoreinvsee") || label.equalsIgnoreCase("ris")) {
			if (args.length == 2) {
				if (sender instanceof Player) {
					if (parseInt(args[1]) != -1) {
						Player player = (Player) sender;
						if (player.hasPermission("restoreinvsee")) {
							@SuppressWarnings("deprecation")
							OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
							String playerUUID = offlinePlayer.getUniqueId().toString();
							ConfigurationSection sectionDeaths = ConfigManager.getConfig()
									.getConfigurationSection("players." + playerUUID + "." + args[1] + ".items");
							// Get data from deaths.yml if there is any
							if (sectionDeaths != null) {
								player.sendMessage(ChatColor.GOLD + args[0] + "'s deaths:");
								for (String death : sectionDeaths.getKeys(false)) {

								}
							} else {

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

	private int parseInt(String string) {
		int x = -1;
		try {
			x = Integer.parseInt(string);
		} catch (Exception ex) {
		}
		return x;
	}

}
