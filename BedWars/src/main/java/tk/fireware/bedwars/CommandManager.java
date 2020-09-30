package tk.fireware.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.io.*;

public class CommandManager implements CommandExecutor {

    public CommandManager () {
        Main.plugin.getCommand("build").setExecutor(this);
        Main.plugin.getCommand("start").setExecutor(this);
        Main.plugin.getCommand("saveworld").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("build")) {
            return build(sender,args);
        } else if(command.getName().equals("start")) {
            return start(sender);
        } else if(command.getName().equals("saveworld")) {
            return saveworld(sender);
        }

        return false;
    }

    private boolean saveworld(CommandSender sender) {
        if(sender.hasPermission("bedwars.saveworld") || sender.isOp()) {
            copyWorld(sender);
            return true;
        }

        return false;
    }

    private void copyWorld(CommandSender sender) {

        Data.getInstance().serialize();

        try {
            Bukkit.getServer().getWorld(R.WORLDNAME).save();
                Runtime.getRuntime().exec("mkdir backup/");
                Runtime.getRuntime().exec("cp -r world/ backup/");
                sender.sendMessage("Die Welt wurde gespeichert!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private boolean start(CommandSender sender) {
        if(!sender.hasPermission("bedwars.start")&&!sender.isOp()) {
            return false;
        }
        Countdown.instance.startGame();
        return true;
    }

    private boolean build(CommandSender sender, String[] args) {
        if(!sender.hasPermission("bedwars.build")&&!sender.isOp()) {
            return false;
        }
        if(args!=null && args.length == 1) {
            BuildManager.getInstance().setBuilder(Bukkit.getPlayer(args[0]), !BuildManager.getInstance().isBuilder((Player)sender));
            if(BuildManager.getInstance().isBuilder(Bukkit.getPlayer(args[0]))) {
                sender.sendMessage(R.PREFIX + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " is now in build-mode!");
            } else {
                sender.sendMessage(R.PREFIX + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " is not in build-mode anymore!");
            }
            return true;
        } else {
            BuildManager.getInstance().setBuilder(Main.plugin.getServer().getPlayer(sender.getName()),!BuildManager.getInstance().isBuilder((Player)sender));
            return true;
        }

    }




}
