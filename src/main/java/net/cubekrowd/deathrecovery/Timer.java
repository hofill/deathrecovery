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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.ConfigurationSection;

public class Timer {

    ConfigManager cfg;
    
    public Timer(String delay, String timeToRemove, ConfigManager cfg) {
        this.cfg = cfg;
        if(delay == null || timeToRemove == null){
            Main.tellConsole("Please fill out config.yml!");
            return;
        }
        if (delay.isEmpty() || timeToRemove.isEmpty()) {
            Main.tellConsole("Please fill out config.yml!");
        } else {
            long delayMilli = getMillis(delay);
            long timeToRemoveMilli = getMillis(timeToRemove);
            final Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    int counter = 0;
                    ConfigurationSection sectionPlayers = cfg.getConfig().getConfigurationSection("players");
                    if (sectionPlayers != null) {
                        for (String key : sectionPlayers.getKeys(false)) {
                            ConfigurationSection sectionDeaths = cfg.getConfig()
                                    .getConfigurationSection("players." + key);
                            if (sectionDeaths != null) {
                                for (String death : sectionDeaths.getKeys(false)) {
                                    String dateDeathString = cfg.getConfig()
                                            .getString("players." + key + "." + death + ".server_time");
                                    Date dateDeath = new Date();
                                    try {
                                        dateDeath = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateDeathString);
                                    } catch (Exception ex) {
                                    }
                                    Date serverDate = new Date();

                                    if (timeToRemoveMilli <= serverDate.getTime() - dateDeath.getTime()) {
                                        counter++;
                                        cfg.getConfig().getConfigurationSection("players." + key).set(death,
                                                null);
                                        cfg.saveConfig();
                                        cfg.reloadConfig();
                                    }
                                }
                            }
                        }
                        Main.tellConsole("[DeathRecovery] Removed " + counter + " inventories!");
                    }
                }
            };

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleWithFixedDelay(runnable, 0, delayMilli, TimeUnit.MILLISECONDS);
        }

    }

    private long getMillis(String stringToConvert) {
        int multipiler = 100000;
        if (stringToConvert.contains("w")) {
            multipiler = 604800000;
            stringToConvert = stringToConvert.replace("w", "");
        }
        if (stringToConvert.contains("d")) {
            multipiler = 86400000;
            stringToConvert = stringToConvert.replace("d", "");
        }
        if (stringToConvert.contains("h")) {
            multipiler = 3600000;
            stringToConvert = stringToConvert.replace("h", "");
        }
        if (stringToConvert.contains("m")) {
            multipiler = 60000;
            stringToConvert = stringToConvert.replace("m", "");
        }
        if (stringToConvert.contains("s")) {
            multipiler = 1000;
            stringToConvert = stringToConvert.replace("s", "");
        }
        return parseInt(stringToConvert) * multipiler;
    }

    private static int parseInt(String string) {
        int x = -1;
        try {
            x = Integer.parseInt(string);
        } catch (Exception ex) {
        }
        return x;
    }

}
