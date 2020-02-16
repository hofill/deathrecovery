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

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.cubekrowd.deathrecovery.Main;

public class ConfigManager {

    private Main plugin = Main.getPlugin(Main.class);
    private File file;
    private FileConfiguration deathsFile;

    public void setup() {
        file = new File(plugin.getDataFolder(), "deaths.yml");

        if (!file.exists()) {
            try {
                plugin.saveResource("deaths.yml", false);
            } catch (Exception ex) {
                Main.tellConsole("Cannot create deaths.yml!");
            }
        }

        deathsFile = YamlConfiguration.loadConfiguration(file);

    }

    public FileConfiguration getConfig() {
        return deathsFile;
    }

    public void saveConfig() {
        try {
            deathsFile.save(file);
        } catch (Exception ex) {
        }
    }

    public void reloadConfig() {
        deathsFile = YamlConfiguration.loadConfiguration(file);
    }

}
