/*
 * Copyright (C) 2019-2020 Chormi (hofill)
 *
 * This file is part of DeathRecovery.
 *
 * DeathRecovery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DeathRecovery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with DeathRecovery.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.cubekrowd.deathrecovery.commands;

import net.cubekrowd.deathrecovery.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.cubekrowd.deathrecovery.ConfigManager;
import net.md_5.bungee.api.ChatColor;

public class RestoreInvGive implements CommandExecutor {

    private ConfigManager cfg;
    
    public RestoreInvGive(ConfigManager cfg) {
        this.cfg = cfg;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("restoreinvgive") || label.equalsIgnoreCase("rig")) {
            if (!(args.length >= 2 && args.length <= 3)) {
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("deathrecovery.restoregive")) {
                player.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }
            
            int deathID = 0;
            try {
                deathID = Integer.parseInt(args[1]);
            } catch(NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "The death ID is an integer!");
                return false;
            }
            if (deathID <= 0) {
                sender.sendMessage(ChatColor.RED + "The death ID is a number bigger than 0!");
                return false;
            }
            
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            OfflinePlayer playerToAdd = null;
            String playerUUID = offlinePlayer.getUniqueId().toString();
            ConfigurationSection sectionDeaths = cfg.getConfig()
                    .getConfigurationSection("players." + playerUUID + "." + args[1] + ".items");
            if (args.length == 3) {
                playerToAdd = Bukkit.getOfflinePlayer(args[2]);
            } else {
                playerToAdd = Bukkit.getOfflinePlayer(args[0]);
            }
            // Check if there are any items in the death inventory.
            if (sectionDeaths != null) {
                // Check if player is online.
                if (playerToAdd.isOnline()) {
                    // Check if his inventory is empty.
                    if (getFull(playerToAdd.getPlayer().getInventory())) {
                        // Add items to the player's inventory
                        fillInv(sectionDeaths, playerUUID, playerToAdd.getPlayer(), args[1]);
                        player.sendMessage(ChatColor.GREEN + "Restored " + offlinePlayer.getName()
                                + "'s inventory to " + playerToAdd.getName() + " successfully!");
                    } else {
                        player.sendMessage(ChatColor.RED + playerToAdd.getName() + "'s inventory is not empty!");
                    }
                    // If player is offline, wait for him to come online
                } else {
                    String offlinePlayerUUID = playerToAdd.getUniqueId().toString();
                    ConfigurationSection sectionAddOffline = cfg.getConfig()
                            .getConfigurationSection("offline_players." + offlinePlayerUUID);
                    if (sectionAddOffline == null) {
                        cfg.getConfig().set("offline_players." + offlinePlayerUUID + ".inv_name",
                                playerUUID);
                        cfg.getConfig().set("offline_players." + offlinePlayerUUID + ".death_id",
                                args[1]);
                        cfg.saveConfig();
                        cfg.reloadConfig();
                        player.sendMessage(ChatColor.GREEN + "Added " + playerToAdd.getName()
                                + " to the waiting list successfully!");
                    } else {
                        player.sendMessage(
                                ChatColor.RED + "This player was already going to recieve his inventory!");
                    }
                }
            } else {
                if(cfg.getConfig().getString("players." + playerUUID + "." + args[1] + ".item_count") != null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "The inventory is empty for that death ID!");
                    return true;
                }
                player.sendMessage(ChatColor.RED + "The player or the death ID does not exist!");
                return true;
            }
        }
        return true;
    }

    public void fillInv(ConfigurationSection cs, String playerUUID, Player playerToAdd, String death_id) {
        for (String items : cs.getKeys(false)) {
            int itemIndex = parseInt(items);
            ItemStack itemToAdd = cfg.getConfig()
                    .getItemStack("players." + playerUUID + "." + death_id + ".items." + items);
            playerToAdd.getInventory().setItem(itemIndex, itemToAdd);
        }
    }

    private boolean getFull(Inventory inv) {
        for (ItemStack item : inv.getContents()) {
            if (item != null)
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
