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

package net.cubekrowd.deathrecovery;

import net.cubekrowd.deathrecovery.commands.Teleport;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.cubekrowd.deathrecovery.commands.Deaths;
import net.cubekrowd.deathrecovery.commands.RestoreInvGive;
import net.cubekrowd.deathrecovery.commands.RestoreInvSee;
import net.cubekrowd.deathrecovery.events.InventoryClick;
import net.cubekrowd.deathrecovery.events.PlayerDeath;
import net.cubekrowd.deathrecovery.events.PlayerJoin;

public class Main extends JavaPlugin {

    private ConfigManager cfg;
    
	public void onEnable() {
		registerConfig();
		registerDeathConfig();
		registerEvents();
		registerCommands();
		new Timer(getConfig().getString("time_between_checks"), getConfig().getString("oldest_death_allowed"),cfg);
	}

	public static void tellConsole(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg);
	}

	public void registerCommands() {
		getCommand("deaths").setExecutor(new Deaths(cfg));
		getCommand("restoreinvsee").setExecutor(new RestoreInvSee(cfg));
		getCommand("restoreinvgive").setExecutor(new RestoreInvGive(cfg));
		getCommand("drteleport").setExecutor(new Teleport());
	}

	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerDeath(cfg), this);
		getServer().getPluginManager().registerEvents(new InventoryClick(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoin(cfg), this);
	}

	public void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}

	public void registerDeathConfig() {
	    cfg = new ConfigManager();
	    cfg.setup();
	    cfg.getConfig().options().copyDefaults(true);
	    cfg.saveConfig();
	}

}
