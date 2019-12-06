package net.cubekrowd.deathrecovery.commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.cubekrowd.deathrecovery.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class Deaths implements CommandExecutor {

    ConfigManager cfg;
    
    public Deaths(ConfigManager cfg) {
        this.cfg = cfg;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("deaths")) {
            if (args.length != 1) {
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            Player player = (Player) sender;
            if (player.hasPermission("deathrecovery.deaths")) {
//						ArrayList<String> deathList = new ArrayList<String>();
                @SuppressWarnings("deprecation")
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                String playerUUID = offlinePlayer.getUniqueId().toString();
                ConfigurationSection sectionDeaths = cfg.getConfig()
                        .getConfigurationSection("players." + playerUUID);
                // Get data from deaths.yml if there is any
                if (sectionDeaths != null) {
                    player.sendMessage(ChatColor.GOLD + args[0] + "'s deaths:");
                    for (String death : sectionDeaths.getKeys(false)) {
//								ConfigurationSection sectionDeathDetails = ConfigManager.getConfig()
//										.getConfigurationSection("players." + playerUUID + "." + death);
                        String death_x = cfg.getConfig()
                                .getString("players." + playerUUID + "." + death + ".death_x");
                        String death_y = cfg.getConfig()
                                .getString("players." + playerUUID + "." + death + ".death_y");
                        String death_z = cfg.getConfig()
                                .getString("players." + playerUUID + "." + death + ".death_z");
                        String world = cfg.getConfig()
                                .getString("players." + playerUUID + "." + death + ".world");
                        String death_type = cfg.getConfig()
                                .getString("players." + playerUUID + "." + death + ".death_type");
                        String item_count = cfg.getConfig()
                                .getString("players." + playerUUID + "." + death + ".item_count");
                        String server_time = cfg.getConfig()
                                .getString("players." + playerUUID + "." + death + ".server_time");
                        // Get difference of time
                        String time_difference = getTimeDifference(server_time);
                        String coordinates = String.format(" X=%s Y=%s Z=%s", death_x, death_y, death_z);
                        // Send message with death info
                        player.spigot().sendMessage(new ComponentBuilder("#" + death + " ").color(ChatColor.WHITE)
                                .bold(true).append(time_difference + death_type + " at").color(ChatColor.GRAY)
                                .bold(false).append(coordinates).color(ChatColor.DARK_AQUA).bold(true)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder("Click to teleport!").color(ChatColor.BLUE).create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                        "/drteleport " + death_x + " " + death_y + " " + death_z + " " + world))
                                .append(" (" + item_count + " items)").color(ChatColor.DARK_AQUA).bold(true)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder("Click to show inventory!").color(ChatColor.BLUE)
                                                .create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                        "/restoreinvsee " + args[0] + " " + death))
                                .create());
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "The player " + args[0] + " has no history of deaths saved.");
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission!");
                return true;
            }
        }

        return true;
    }

    private static String getTimeDifference(String system_time) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime firstDate = LocalDateTime.parse(system_time, format);
        LocalDateTime secondDate = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime cut = firstDate.plusDays(ChronoUnit.DAYS.between(firstDate, secondDate));
        Period period = Period.between(firstDate.toLocalDate(), cut.toLocalDate());
        Duration duration = Duration.between(cut, secondDate);
        String result = String.format("%sy %smt %sd %sh %sm %ss ago: ", period.getYears(), period.getMonths(),
                period.getDays(), duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
        // Removes time info if 0
        if (period.getYears() == 0) {
            result = result.replace("0y ", "");
        }
        if (period.getMonths() == 0) {
            result = result.replace("0mt ", "");
        }
        if (period.getDays() == 0) {
            result = result.replace("0d ", "");
        }
        if (duration.toHours() == 0) {
            result = result.replace("0h ", "");
        }
        if (duration.toMinutesPart() == 0) {
            result = result.replace("0m ", "");
        }
        return result;
    }

}
