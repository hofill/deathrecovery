package net.cubekrowd.deathrecovery.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleport implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /drteleport <x> <y> <z> <world>
        if(label.equalsIgnoreCase("drteleport")){
            if (!(args.length == 4)) {
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("deathrecovery.drteleport")) {
                player.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }
            World world = Bukkit.getWorld(args[3]);
            int x,y,z;
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
            Location newLocation = new Location(world, x, y, z);
            if(y < 1) {
                player.sendMessage(ChatColor.RED + "The player died too far down!");
                return true;
            }
            player.teleport(newLocation);
            player.setGameMode(GameMode.SPECTATOR);
        }

        return true;
    }
}
