package com.hofill.deathrecovery.events;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import net.md_5.bungee.api.ChatColor;

public class InventoryClick implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (event.getView().getTitle().contains("death inventory.")) {
				// Set accept/decline item
				ItemStack accept = new ItemStack(Material.GREEN_WOOL);
				ItemStack reject = new ItemStack(Material.RED_WOOL);
				ItemMeta acceptMeta = accept.getItemMeta();
				ItemMeta rejectMeta = reject.getItemMeta();
				acceptMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aSend to player"));
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
				Inventory inventory = event.getClickedInventory();
				if (inventory.getItem(7).equals(accept) && inventory.getItem(8).equals(reject)) {
					event.setCancelled(true);
					if(event.getCurrentItem().equals(accept)) {
						player.closeInventory();
						String title = event.getView().getTitle();
						String splitTitle[] = title.split(" ");
						String playerName = title.substring(0, title.indexOf("'"));
						//Get deathID
						String deathID = splitTitle[1].replace("#", "");
						player.performCommand("restoreinvgive " + playerName + " " + deathID);
					} else if(event.getCurrentItem().equals(reject)) {
						player.closeInventory();
					}
					event.setCancelled(true);
				}
			}
		}
	}

}
