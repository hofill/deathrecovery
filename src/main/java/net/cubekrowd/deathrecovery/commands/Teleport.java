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

import java.util.UUID;

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

            World world;

            try {
                UUID worldUUID = UUID.fromString(args[3]);
                world = Bukkit.getWorld(worldUUID);
            } catch (Exception ex){
                player.sendMessage(ChatColor.RED + "World UUID is incorrect!");
                return true;
            }

            int x=0,y=0,z=0;

            try {
                x = Integer.parseInt(args[0]);
                y = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex){
                player.sendMessage(ChatColor.RED + "The coordinates must be numbers!");
                return true;
            }
            Location newLocation = new Location(world, x, y, z);
            if(y < 1) {
                player.sendMessage(ChatColor.RED + "The player died too far down!");
                return true;
            }
            if(world != null){
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(newLocation);
            } else {
                player.sendMessage(ChatColor.RED + "The world is not loaded!");
            }

        }

        return true;
    }
}
