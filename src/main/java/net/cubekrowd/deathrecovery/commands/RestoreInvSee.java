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

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.cubekrowd.deathrecovery.ConfigManager;
import net.md_5.bungee.api.ChatColor;

public class RestoreInvSee implements CommandExecutor {

    private ConfigManager cfg;
    
    public RestoreInvSee(ConfigManager cfg) {
        this.cfg = cfg;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("restoreinvsee") || label.equalsIgnoreCase("ris")) {
            if (args.length != 2) {
                return false;   
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;   
            }
            Player player = (Player) sender;
            if (!player.hasPermission("deathrecovery.restoresee")) {
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
            
            // -- MAIN CODE -- \\
            @SuppressWarnings("deprecation")
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            String playerUUID = offlinePlayer.getUniqueId().toString();
            // Get data from deaths.yml if there is any
            ConfigurationSection sectionDeaths = cfg.getConfig()
                    .getConfigurationSection("players." + playerUUID + "." + args[1] + ".items");
            Inventory inventory = Bukkit.createInventory(null, 45,
                    args[0] + "'s #" + args[1] + " death inventory.");
            ItemStack[][] inventoryPosition = new ItemStack[5][10];
            if (sectionDeaths == null) {
                if(cfg.getConfig().getString("players." + playerUUID + "." + args[1] + ".item_count") != null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "The inventory is empty for that death ID!");
                    return true;
                }
                player.sendMessage(ChatColor.RED + "The player or the death ID does not exist!");
                return true;
            }
            for (String items : sectionDeaths.getKeys(false)) {
                int itemIndex = 0;
                itemIndex = Integer.parseInt(items);
                if (itemIndex == 40)
                    itemIndex++;
                inventoryPosition[itemIndex / 9][itemIndex % 9] = cfg.getConfig()
                        .getItemStack("players." + playerUUID + "." + args[1] + ".items." + items);
            }
            // Flip items to match inventory's style
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    ItemStack temp = inventoryPosition[i][j];
                    inventoryPosition[i][j] = inventoryPosition[4 - i][j];
                    inventoryPosition[4 - i][j] = temp;
                }
            }

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 9; j++) {
                    inventory.setItem((i * 9) + j, inventoryPosition[i][j]);
                }
            }
            // Set filler item
            ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
            // Set accept/decline item
            ItemStack accept = new ItemStack(Material.GREEN_WOOL);
            ItemStack reject = new ItemStack(Material.RED_WOOL);
            ItemMeta acceptMeta = accept.getItemMeta();
            ItemMeta rejectMeta = reject.getItemMeta();
            acceptMeta.setDisplayName(
                    ChatColor.translateAlternateColorCodes('&', "&aSend to player"));
            rejectMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cClose"));
            ArrayList<String> loreAccept = new ArrayList<String>();
            ArrayList<String> loreReject = new ArrayList<String>();
            loreAccept.add(ChatColor.BLUE + "Sends the current death inventory");
            loreAccept.add(ChatColor.BLUE + "to the player's inventory.");
            loreReject.add(ChatColor.BLUE + "Closes the death inventory.");
            acceptMeta.setLore(loreAccept);
            rejectMeta.setLore(loreReject);
            accept.setItemMeta(acceptMeta);
            reject.setItemMeta(rejectMeta);
            inventory.setItem(4, filler);
            inventory.setItem(6, filler);
            inventory.setItem(7, accept);
            inventory.setItem(8, reject);
            player.openInventory(inventory);
        }

        return true;
    }

}
