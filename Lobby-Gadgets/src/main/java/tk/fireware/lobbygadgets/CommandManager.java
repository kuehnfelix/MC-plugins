package tk.fireware.lobbygadgets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    private static CommandManager instance;

    public static CommandManager getInstance() {
        if(instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getLabel().equalsIgnoreCase("warp") && sender instanceof Player) {
            if(args.length == 1) {
                if(((Player)sender).hasPermission("lobby.warp")) {
                    if (Data.instance.getWarp(args[0]) != null) {
                        ((Player) sender).teleport(Data.instance.getWarp(args[0]));
                        sender.sendMessage(Plugin.instance.getConfig().getString("MESSAGES.warped".replaceAll("<name>", args[0]), ChatColor.GREEN + "[WARP] Warped to " + ChatColor.GOLD + args[0]));
                    } else {
                        sender.sendMessage(Plugin.instance.getConfig().getString("MESSAGES.warpnotexist".replaceAll("<name>", args[0]), ChatColor.RED + "[WARP] Warp " + ChatColor.GOLD + args[0]) + ChatColor.RED + " doesn't exist!");
                    }
                    return true;
                }
                sender.sendMessage(Plugin.instance.getConfig().getString("MESSAGES.noPermission".replaceAll("<permission>", "lobby.setwarp"), ChatColor.RED + "You do not have Permission: lobby.warp"));
            } else if (args.length == 2) {
                if(sender.hasPermission("lobby.setwarp")) {
                    if(Data.instance.setWarp(args[1], ((Player) sender).getLocation())) {
                        sender.sendMessage(Plugin.instance.getConfig().getString("MESSAGES.setWarp".replaceAll("<name>", args[0]), ChatColor.GREEN + "[WARP] Warp " + ChatColor.GOLD + args[1])+ ChatColor.GREEN + " has been set!");
                    } else {
                        sender.sendMessage(Plugin.instance.getConfig().getString("MESSAGES.setWarp".replaceAll("<name>", args[0]), ChatColor.RED + "[WARP] Warp " + ChatColor.GOLD + args[1])+ ChatColor.RED + " couldn't be set!");
                    }
                    return true;
                }
                sender.sendMessage(Plugin.instance.getConfig().getString("MESSAGES.noPermission".replaceAll("<permission>", "lobby.setwarp"), ChatColor.RED + "You do not have Permission: lobby.warp"));
            }
            return false;
        } else if(command.getLabel().equalsIgnoreCase("updateresourcepack") && sender instanceof Player) {
            if(args.length == 1) {
                if(((Player)sender).hasPermission("lobby.updateresourcepack")) {
                    Plugin.instance.getConfig().set("HASRESOURCEPACK", "");
                    return true;
                }
                sender.sendMessage(Plugin.instance.getConfig().getString("MESSAGES.noPermission".replaceAll("<permission>", "lobby.setwarp"), ChatColor.RED + "You do not have Permission: lobby.warp"));
            }
            return false;
        }
        return false;
    }
}
