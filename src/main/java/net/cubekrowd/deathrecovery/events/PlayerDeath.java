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

package net.cubekrowd.deathrecovery.events;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.cubekrowd.deathrecovery.ConfigManager;

public class PlayerDeath implements Listener {

    private int deathId = 1;
    private ConfigManager cfg;
    
    public PlayerDeath(ConfigManager cfg) {
        this.cfg = cfg;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ConfigurationSection sectionPlayers = cfg.getConfig().getConfigurationSection("players");
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
            ConfigurationSection sectionUUID = cfg.getConfig()
                    .getConfigurationSection("players." + playerUUID);
            if (sectionUUID != null) {
                for (String key : sectionUUID.getKeys(false)) {
                    deaths.add(key);
                }
                if (sectionUUID.getKeys(false).size() > 0)
                    deathId = parseInt(deaths.get(deaths.size() - 1)) + 1;
            }
        }
        // Fill up data in deaths.yml after death
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
        cfg.getConfig().set("players." + playerUUID + "." + deathId + ".death_x",
                player.getLocation().getBlockX());
        cfg.getConfig().set("players." + playerUUID + "." + deathId + ".death_y",
                player.getLocation().getBlockY());
        cfg.getConfig().set("players." + playerUUID + "." + deathId + ".death_z",
                player.getLocation().getBlockZ());
        cfg.getConfig().set("players." + playerUUID + "." + deathId + ".world",
                player.getLocation().getWorld().getUID().toString());
        cfg.getConfig().set("players." + playerUUID + "." + deathId + ".death_type", event.getDeathMessage());
        cfg.getConfig().set("players." + playerUUID + "." + deathId + ".server_time",
                currentDateTime.format(format));
        int inventoryCount = 0;
        int indexItems = 0;
        // Get items from inventory and add them to deaths.yml
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null) {
                cfg.getConfig().set("players." + playerUUID + "." + deathId + ".items." + indexItems, i);
                inventoryCount++;
            }
            indexItems++;
        }
        cfg.getConfig().set("players." + playerUUID + "." + deathId + ".item_count", inventoryCount);
        cfg.saveConfig();
        cfg.reloadConfig();

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
