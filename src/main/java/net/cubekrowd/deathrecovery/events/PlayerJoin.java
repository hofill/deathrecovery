package net.cubekrowd.deathrecovery.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.cubekrowd.deathrecovery.ConfigManager;
import net.md_5.bungee.api.ChatColor;

public class PlayerJoin implements Listener {

    private ConfigManager cfg;
    
    public PlayerJoin(ConfigManager cfg) {
        this.cfg = cfg;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();
        ConfigurationSection sectionPlayers = cfg.getConfig()
                .getConfigurationSection("offline_players." + playerUUID);
        if (sectionPlayers != null) {
            // See if the player's inventory has items in it
            if (getFull(player.getInventory())) {
                String death_id = cfg.getConfig().getString("offline_players." + playerUUID + ".death_id");
                String inv_name = cfg.getConfig().getString("offline_players." + playerUUID + ".inv_name");
                ConfigurationSection sectionItems = cfg.getConfig()
                        .getConfigurationSection("players." + inv_name + "." + death_id + ".items");
                if (sectionItems != null) {
                    fillInv(sectionItems, playerUUID, player, death_id);
                    player.sendMessage(ChatColor.GREEN + "Successfully recovered your inventory!");
                    removeFromYAML(playerUUID);
                } else {
                    player.sendMessage(ChatColor.RED + "Your death inventory expired or is invalid!");
                    removeFromYAML(playerUUID);
                }

            } else {
                player.sendMessage(
                        ChatColor.RED + "Could not restore your death inventory because your inventory is full!");
                player.sendMessage(ChatColor.RED + "Clear out your inventory and log back in!");
            }
        }
    }

    private void removeFromYAML(String playerUUID) {
        cfg.getConfig().getConfigurationSection("offline_players").set(playerUUID, null);
        cfg.saveConfig();
        cfg.reloadConfig();
    }
    
    private void fillInv(ConfigurationSection cs, String playerUUID, Player playerToAdd, String death_id) {
        for (String items : cs.getKeys(false)) {
            int itemIndex = parseInt(items);
            ItemStack itemToAdd = cfg.getConfig()
                    .getItemStack("players." + playerUUID + "." + death_id + ".items." + items);
            playerToAdd.getInventory().setItem(itemIndex, itemToAdd);
        }
    }
    
    private int parseInt(String string) {
        int x = -1;
        try {
            x = Integer.parseInt(string);
        } catch (Exception ex) {
        }
        return x;
    }

    private boolean getFull(Inventory inv) {
        for (ItemStack item : inv.getContents()) {
            if (item != null)
                return false;
        }
        return true;
    }

}
