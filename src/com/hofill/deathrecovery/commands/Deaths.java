package com.hofill.deathrecovery.commands;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.hofill.deathrecovery.ConfigManager;

import net.md_5.bungee.api.ChatColor;

public class Deaths implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (label.equalsIgnoreCase("deaths")) {
			if (args.length == 1) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasPermission("deathrecovery.deaths")) {
//						ArrayList<String> deathList = new ArrayList<String>();
						@SuppressWarnings("deprecation")
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
						String playerUUID = offlinePlayer.getUniqueId().toString();
						ConfigurationSection sectionDeaths = ConfigManager.getConfig()
								.getConfigurationSection("players." + playerUUID);
						//Get data from deaths.yml if there are any
						if (sectionDeaths != null) {
							for (String death : sectionDeaths.getKeys(false)) {
//								ConfigurationSection sectionDeathDetails = ConfigManager.getConfig()
//										.getConfigurationSection("players." + playerUUID + "." + death);
								int[] x = null;
								String death_x = ConfigManager.getConfig().getString("players." + playerUUID + "." + death + ".death_x");
								String death_y = ConfigManager.getConfig().getString("players." + playerUUID + "." + death + ".death_x");
								String death_z = ConfigManager.getConfig().getString("players." + playerUUID + "." + death + ".death_x");
								String death_type = ConfigManager.getConfig().getString("players." + playerUUID + "." + death + ".death_x");
								String item_count = ConfigManager.getConfig().getString("players." + playerUUID + "." + death + ".death_x");
								String system_time = ConfigManager.getConfig().getString("players." + playerUUID + "." + death + ".death_x");
								//Get difference of time
								try {
									Date timeAgo = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(system_time);
									Date timeNow = new Date();
									LocalDate firstDate = timeAgo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
									LocalDate secondDate = timeNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
								} catch (Exception ex) {
								}
								
								
								player.sendMessage(ChatColor.GOLD + args[0] + "'s deaths:");
								player.sendMessage(ChatColor.WHITE + death + "." + ChatColor.GRAY + death_type + " at X=" + death_x + " Y=" + death_y + " Z=" + death_z );
							}
						} else {
							player.sendMessage(
									ChatColor.RED + "The player " + args[0] + " has never died on the server.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "You do not have permission!");
					}
				} else {
					sender.sendMessage("Only players can use this command!");
				}
			} else {
				return false;
			}
		}

		return true;
	}

	private static Calendar getCalendar(Date date) {
	    Calendar cal = Calendar.getInstance(Locale.US);
	    cal.setTime(date);
	    return cal;
	}
	
}
